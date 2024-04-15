package de.solugo.oidc.util

import org.jose4j.jwt.JwtClaims

private const val CLAIM_SCOPE = "scope"
private const val CLAIM_SESSION_ID = "sid"
private const val CLAIM_CLIENT_ID = "client_id"
private const val CLAIM_PREFERRED_USERNAME = "preferred_username"

var JwtClaims.scopes: Set<String>?
    get() = run {
        getStringClaimValue(CLAIM_SCOPE)?.split(" ")?.toSet()
    }
    set(value) {
        when {
            value == null -> unsetClaim(CLAIM_SCOPE)
            else -> setClaim(CLAIM_SCOPE, value.joinToString(" "))
        }
    }

var JwtClaims.preferredUsername: String?
    get() = run {
        getStringClaimValue(CLAIM_PREFERRED_USERNAME)
    }
    set(value) {
        when {
            value == null -> unsetClaim(CLAIM_PREFERRED_USERNAME)
            else -> setClaim(CLAIM_PREFERRED_USERNAME, value)
        }
    }

var JwtClaims.clientId: String?
    get() = run {
        getStringClaimValue(CLAIM_CLIENT_ID)
    }
    set(value) {
        when {
            value == null -> unsetClaim(CLAIM_CLIENT_ID)
            else -> setClaim(CLAIM_CLIENT_ID, value)
        }
    }

var JwtClaims.sessionId: String?
    get() = run {
        getStringClaimValue(CLAIM_SESSION_ID)
    }
    set(value) {
        when {
            value == null -> unsetClaim(CLAIM_SESSION_ID)
            else -> setClaim(CLAIM_SESSION_ID, value)
        }
    }

operator fun JwtClaims.plus(other: JwtClaims) = when {
    this.claimsMap.isEmpty() -> other
    other.claimsMap.isEmpty() -> this
    else -> JwtClaims().also {
        this.claimsMap.forEach { (key, value) -> it.setClaim(key, value) }
        other.claimsMap.forEach { (key, value) -> it.setClaim(key, value) }
    }
}