package de.solugo.oidc.grant

import de.solugo.oidc.util.badRequest
import de.solugo.oidc.util.clientId
import de.solugo.oidc.util.scope
import org.jose4j.jwt.JwtClaims
import org.springframework.stereotype.Component
import org.springframework.util.MultiValueMap

@Component
class PasswordGrant : Grant {
    override val type: String = "password"

    override suspend fun process(params: MultiValueMap<String, String>, claims: JwtClaims) {
        val clientId = params.getFirst("client_id") ?: badRequest("Missing client_id")
        val username = params.getFirst("username") ?: badRequest("Missing username")
        val scope = params.getFirst("scope")

        claims.subject = username
        claims.clientId = clientId
        claims.scope = scope
    }
}