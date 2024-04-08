package de.solugo.oidc.grant

import org.jose4j.jwt.JwtClaims
import org.springframework.stereotype.Component
import org.springframework.util.MultiValueMap

@Component
class PasswordGrant : Grant {
    override val type: String = "password"

    override suspend fun process(params: MultiValueMap<String, String>, claims: JwtClaims) {

    }
}