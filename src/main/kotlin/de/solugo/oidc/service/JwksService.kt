package de.solugo.oidc.service

import de.solugo.oidc.util.uuid
import org.jose4j.jwk.RsaJwkGenerator
import org.springframework.stereotype.Service

@Service
class JwksService {

    val keys by lazy {
        listOf(
            RsaJwkGenerator.generateJwk(2048).apply {
                keyId = uuid()
                use = "sig"
                algorithm = "RS256"
            }
        )
    }

}