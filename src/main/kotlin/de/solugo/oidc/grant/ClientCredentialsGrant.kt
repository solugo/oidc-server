package de.solugo.oidc.grant

import org.jose4j.jwt.JwtClaims
import org.springframework.stereotype.Component
import org.springframework.util.MultiValueMap

@Component
class ClientCredentialsGrant : Grant {
    override val type: String = "client_credentials"

    override suspend fun process(params: MultiValueMap<String, String>, claims: JwtClaims) {
    }
}