package de.solugo.oidc

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("server")
data class ServerProperties(
    val claims: Map<String, Any?> = emptyMap(),
)