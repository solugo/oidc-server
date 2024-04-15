package de.solugo.oidc.service

import de.solugo.oidc.ServerProperties
import de.solugo.oidc.util.uuid
import org.jose4j.jwk.JsonWebKey
import org.jose4j.jwk.PublicJsonWebKey
import org.jose4j.jws.JsonWebSignature
import org.jose4j.jwt.JwtClaims
import org.jose4j.jwt.consumer.JwtConsumerBuilder
import org.springframework.stereotype.Service

@Service
class TokenService(
    private val keySetService: KeySetService,
    private val properties: ServerProperties,
) {

    private val consumer = JwtConsumerBuilder().run {
        setVerificationKeyResolver { signature, _ ->
            keySetService.keys.first { it.keyId == signature.keyIdHeaderValue }.key
        }
        build()
    }

    fun encodeJwt(
        issuer: String,
        claims: JwtClaims,
        webKey: JsonWebKey = keySetService.keys.first(),
    ) = JsonWebSignature().run {
        key = when (webKey) {
            is PublicJsonWebKey -> webKey.privateKey
            else -> error("Webkey $webKey not supported for jwts")
        }
        keyIdHeaderValue = webKey.keyId
        algorithmHeaderValue = webKey.algorithm
        payload = claims.run {
            jwtId = uuid()
            properties.claims.forEach { (key, value) -> setClaim(key, value) }
            setIssuedAtToNow()
            toJson()
        }
        compactSerialization
    }

    fun decodeJwt(
        jwt: String,
    ) = consumer.process(jwt)

}