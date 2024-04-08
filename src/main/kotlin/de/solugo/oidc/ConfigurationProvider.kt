package de.solugo.oidc

import org.springframework.web.util.UriComponentsBuilder

interface ConfigurationProvider {
    fun provide(builder: UriComponentsBuilder): Map<String, Any>
}