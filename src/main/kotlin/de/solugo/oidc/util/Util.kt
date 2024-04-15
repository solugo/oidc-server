package de.solugo.oidc.util

import java.util.*

fun uuid(value: String? = null) = when {
    value != null -> UUID.nameUUIDFromBytes(value.toByteArray()).toString()
    else -> UUID.randomUUID().toString()
}

