package de.solugo.oidc.service

import de.solugo.oidc.ServerProperties
import de.solugo.oidc.util.uuid
import org.jose4j.jws.JsonWebSignature
import org.jose4j.jwt.JwtClaims
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder

@Service
class TokenService(
    private val jwksService: JwksService,
    private val properties: ServerProperties,
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
            jwtId = uuid()
            issuer = issuerUri.toUriString()
            properties.claims.forEach { (key, value) -> setClaim(key, value) }
            setIssuedAtToNow()
            toJson()
        }
        compactSerialization
    }

}