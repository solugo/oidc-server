package de.solugo.oidc.token

import org.springframework.util.MultiValueMap
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class TokenContext(
    issuer: String,
    parameters: MultiValueMap<String, String>,
    scopes: Set<String>? = null,
) {
    internal val attributes = mutableMapOf(
        TokenContext::issuer.name to issuer,
        TokenContext::parameters.name to parameters,
        TokenContext::scopes.name to scopes,
    )

    companion object {
        fun <T> attribute(creator: (() -> T & Any)? = null) = object : ReadWriteProperty<TokenContext, T> {
            @Suppress("unchecked_cast")
            override fun getValue(thisRef: TokenContext, property: KProperty<*>): T = run {
                when {
                    creator != null -> thisRef.attributes.computeIfAbsent(property.name) { creator() } as T
                    else -> thisRef.attributes[property.name] as T
                }
            }

            override fun setValue(thisRef: TokenContext, property: KProperty<*>, value: T) {
                when {
                    value != null -> thisRef.attributes[property.name] = value
                    else -> thisRef.attributes.remove(property.name)
                }
            }

        }
    }
}


