package de.solugo.oidc.service

import org.jose4j.jws.JsonWebSignature
import org.jose4j.jwt.JwtClaims
import org.jose4j.jwt.NumericDate
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder

@Service
class TokenService(
    private val jwksService: JwksService,
) {

    fun createToken(
        issuerUri: UriComponentsBuilder,
        claims: JwtClaims,
    ) = JsonWebSignature().run {
        val webKey = jwksService.keys.first()
        key = webKey.privateKey
        keyIdHeaderValue = webKey.keyId
        algorithmHeaderValue = webKey.algorithm
        payload = claims.run {
            issuer = issuerUri.toUriString()
            issuedAt = NumericDate.now()
            toJson()
        }
        compactSerialization
    }

}