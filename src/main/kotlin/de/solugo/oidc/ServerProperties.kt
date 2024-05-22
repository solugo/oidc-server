package de.solugo.oidc

import de.solugo.oidc.util.uri
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.util.UriComponentsBuilder

@ConfigurationProperties("server")
data class ServerProperties(
    val publicUrl: String? = null,
    val claims: Map<String, Any?> = emptyMap(),
) {
    fun ServerWebExchange.issuerUri(
        block: UriComponentsBuilder.() -> Unit,
    ) = uri(publicUrl ?: request.uri.toString(), block)
}
