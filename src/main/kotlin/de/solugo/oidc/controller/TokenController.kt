package de.solugo.oidc.controller


import de.solugo.oidc.ConfigurationProvider
import de.solugo.oidc.ServerProperties
import de.solugo.oidc.service.TokenService
import de.solugo.oidc.token.*
import de.solugo.oidc.util.plus
import de.solugo.oidc.util.scopes
import de.solugo.oidc.util.sessionId
import de.solugo.oidc.util.uuid
import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.util.UriComponentsBuilder
import java.time.Instant

@RestController
class TokenController(
    private val grants: List<TokenGrant>,
    private val tokenService: TokenService,
    private val tokenProcessors: List<TokenProcessor>,
    private val properties: ServerProperties,
) : ConfigurationProvider {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun provide(exchange: ServerWebExchange) = with(properties) {
        mapOf(
            "token_endpoint" to exchange.issuerUri {
                replacePath("/token")
            }
        )
    }

    @PostMapping("/token", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    suspend fun createToken(
        exchange: ServerWebExchange,
        uriBuilder: UriComponentsBuilder,
    ) = run {
        val parameters = exchange.formData.awaitSingle()

        try {
            process(
                issuer = uriBuilder.replacePath("/").toUriString(),
                parameters = parameters,
            )
        } catch (ex: TokenException) {
            mapOf(
                "error" to ex.error,
                "errorDescription" to ex.description,
                "errorUri" to ex.uri,
            )
        }
    }

    private suspend fun process(
        issuer: String,
        parameters: MultiValueMap<String, String>,
    ): Map<String, Any> = try {
        val type = parameters.getFirst("grant_type") ?: throw TokenException(
            error = TokenError.InvalidRequest,
            description = "Request is missing grant_type parameter"
        )
        val grant = grants.firstOrNull { it.type == type } ?: throw TokenException(
            error = TokenError.InvalidRequest,
            description = "Grant type '$type' is not supported"
        )

        val context = TokenContext(
            issuer = issuer,
            parameters = parameters,
            scopes = parameters.getFirst("scope")?.split(" ")?.toSet(),
        )

        tokenProcessors.process(TokenProcessor.Step.PRE, context)

        grant.process(context)

        tokenProcessors.process(TokenProcessor.Step.POST, context)
        tokenProcessors.process(TokenProcessor.Step.VALIDATE, context)
        tokenProcessors.process(TokenProcessor.Step.CLAIMS, context)

        val commonClaims = context.commonClaims
        val idClaims = commonClaims + context.idClaims
        val accessClaims = commonClaims + context.accessClaims
        val refreshClaims = commonClaims + context.refreshClaims

        listOf(idClaims, accessClaims, refreshClaims).forEach { claims ->
            claims.issuer = issuer
            claims.jwtId = uuid()
            claims.sessionId = claims.sessionId ?: uuid()
        }

        buildMap {
            put("token_type", "Bearer")

            accessClaims.takeIf { it.claimsMap.isNotEmpty() }?.also { claims ->
                put("access_token", tokenService.encodeJwt(context.issuer, claims))

                claims.expirationTime?.also {
                    put("expires_in", it.value - (claims.issuedAt?.value ?: Instant.now().epochSecond))
                }
            }
            refreshClaims.takeIf { it.claimsMap.isNotEmpty() }?.also { claims ->
                if (claims.scopes?.contains("offline_access") != true) return@also
                put("refresh_token", tokenService.encodeJwt(context.issuer, claims))
            }
            idClaims.takeIf { it.claimsMap.isNotEmpty() }?.also { claims ->
                if (claims.scopes?.contains("openid") != true) return@also
                put("id_token", tokenService.encodeJwt(context.issuer, claims))
            }
        }
    } catch (ex: Exception) {
        logger.error("Error processing token request", ex)
        throw TokenException(
            error = TokenError.ServerError,
            description = ex.message,
            cause = ex,
        )
    }

    private suspend fun List<TokenProcessor>.process(step: TokenProcessor.Step, context: TokenContext) {
        this.forEach {
            if (it.step == step) {
                it.process(context)
            }
        }
    }

}
