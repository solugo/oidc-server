package de.solugo.oidc

import org.springframework.web.server.ServerWebExchange

interface ConfigurationProvider {
    fun provide(exchange: ServerWebExchange): Map<String, Any>
}
