package de.solugo.oidc.token.grant

import de.solugo.oidc.service.TokenService
import de.solugo.oidc.token.*
import de.solugo.oidc.util.clientId
import de.solugo.oidc.util.scopes
import org.springframework.stereotype.Component

@Component
class RefreshTokenGrant(
    private val tokenService: TokenService,

    ) : TokenGrant {
    override val type: String = "refresh_token"

    override suspend fun process(context: TokenContext) {
        val client = context.client ?: throw TokenException(
            error = TokenError.UnauthorizedClient,
            description = "Client resolution failed",
        )
        val refreshToken = context.refreshToken ?: throw TokenException(
            error = TokenError.InvalidRequest,
            description = "Request is missing refresh_token parameter",
        )

        val refreshContext = tokenService.decodeJwt(refreshToken)

        if (refreshContext.jwtClaims.clientId != client.id) throw TokenException(
            error = TokenError.AccessDenied,
            description = "Client is not allowed to use this refresh token",
        )

        val refreshScopes = refreshContext.jwtClaims.scopes ?: emptySet()

        context.scopes = context.scopes?.filter { refreshScopes.contains(it) }?.toSet() ?: refreshScopes

        context.commonClaims.apply {
            refreshContext.jwtClaims.claimsMap.forEach { (key, value) -> setClaim(key, value) }
        }

    }
}