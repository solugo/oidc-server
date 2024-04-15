package de.solugo.oidc.model

import java.time.Duration


interface Client {
    val id: String
    val allowedGrants: Set<String>
    val accessTokenLifetime: Duration?
}