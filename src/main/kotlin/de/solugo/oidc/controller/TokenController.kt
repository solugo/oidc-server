package de.solugo.oidc.controller

import de.solugo.oidc.ConfigurationProvider
import de.solugo.oidc.grant.Grant
import de.solugo.oidc.service.TokenService
import de.solugo.oidc.util.badRequest
import kotlinx.coroutines.reactor.awaitSingle
import org.jose4j.jwt.JwtClaims
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.util.UriComponentsBuilder

@RestController
class TokenController(
    private val grants: List<Grant>,
    private val tokenService: TokenService,
) : ConfigurationProvider {
    override fun provide(builder: UriComponentsBuilder) = mapOf(
        "token_endpoint" to builder.replacePath("/token").toUriString(),
    )

    @PostMapping("/token")
    suspend fun createToken(
        exchange: ServerWebExchange,
        uriBuilder: UriComponentsBuilder,
    ) = run {
        val params = exchange.formData.awaitSingle()
        val type = params.getFirst("grant_type") ?: badRequest("Missing grant_type")
        val provider = grants.firstOrNull { it.type == type } ?: badRequest("Grant type '$type' is not supported")
        val claims = JwtClaims()

        provider.process(params, claims)

        mapOf(
            "access_token" to tokenService.createToken(uriBuilder.replacePath("/"), claims)
        )

    }
}