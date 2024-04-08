package de.solugo.oidc.grant

import de.solugo.oidc.util.badRequest
import de.solugo.oidc.util.clientId
import de.solugo.oidc.util.scope
import org.jose4j.jwt.JwtClaims
import org.springframework.stereotype.Component
import org.springframework.util.MultiValueMap

@Component
class ClientCredentialsGrant : Grant {
    override val type: String = "client_credentials"

    override suspend fun process(params: MultiValueMap<String, String>, claims: JwtClaims) {
        val clientId = params.getFirst("client_id") ?: badRequest("Missing client_id")
        val scope = params.getFirst("scope")

        claims.subject = clientId
        claims.clientId = clientId
        claims.scope = scope
    }
}