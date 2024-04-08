package de.solugo.oidc

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("server")
data class ServerProperties(
    val scopes: List<String> = emptyList(),
    val keys: List<Key> = listOf(
        Key(
            name = "default",
            private = "",
            public = "",
        )
    )
) {

    data class Key(
        val name: String,
        val private: String,
        val public: String,
    )
}