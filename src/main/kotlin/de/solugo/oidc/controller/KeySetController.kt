package de.solugo.oidc.controller

import de.solugo.oidc.ConfigurationProvider
import de.solugo.oidc.ServerProperties
import de.solugo.oidc.service.KeySetService
import org.jose4j.jwk.JsonWebKey
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange

@RestController
@ConditionalOnBean(KeySetService::class)
class KeySetController(
    private val keySetService: KeySetService,
    private val properties: ServerProperties,
) : ConfigurationProvider {

    override fun provide(exchange: ServerWebExchange) = with(properties) {
        mapOf(
            "jwks_uri" to exchange.issuerUri {
                replacePath("/.well-known/jwks.json")
            },
        )
    }

    @GetMapping(".well-known/jwks.json")
    fun getJwks() = mapOf(
        "keys" to keySetService.keys.map {
            it.toParams(JsonWebKey.OutputControlLevel.PUBLIC_ONLY)
        }
    )

}
