package de.solugo.oidc.controller

import de.solugo.oidc.ConfigurationProvider
import de.solugo.oidc.ServerProperties
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange

@RestController
class ConfigurationController(
    private val configurationProviders: List<ConfigurationProvider>,
    private val properties: ServerProperties,
) {

    @GetMapping(".well-known/openid-configuration")
    fun configuration(exchange: ServerWebExchange) = with(properties) {
        buildMap {
            put("issuer", exchange.issuerUri {
                replacePath("/")
            })
            configurationProviders.forEach {
                putAll(it.provide(exchange))
            }
        }
    }


}
