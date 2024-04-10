package de.solugo.oidc.controller

import de.solugo.oidc.ConfigurationProvider
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder

@RestController
class ConfigurationController(
    private val configurationProviders: List<ConfigurationProvider>,
) {

    @GetMapping(".well-known/openid-configuration")
    fun configuration(builder: UriComponentsBuilder) = buildMap {
        put("issuer", builder.replacePath("/").toUriString())
        configurationProviders.forEach {
            putAll(it.provide(builder))
        }
    }

}