package de.solugo.oidc.grant

import org.jose4j.jwt.JwtClaims
import org.springframework.util.MultiValueMap

interface Grant {
    val type: String
    suspend fun process(params: MultiValueMap<String, String>, claims: JwtClaims)
}