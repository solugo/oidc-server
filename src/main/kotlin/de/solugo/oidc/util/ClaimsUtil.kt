package de.solugo.oidc.util

import org.jose4j.jwt.JwtClaims

private const val CLAIM_SCOPE = "scope"
private const val CLAIM_CLIENT_ID = "client_id"

var JwtClaims.scope: String?
    get() = run {
        getStringClaimValue(CLAIM_SCOPE)
    }
    set(value) {
        when (value) {
            null -> unsetClaim(CLAIM_SCOPE)
            else -> setClaim(CLAIM_SCOPE, value)
        }
    }

var JwtClaims.clientId: String?
    get() = run {
        getStringClaimValue(CLAIM_CLIENT_ID)
    }
    set(value) {
        when (value) {
            null -> unsetClaim(CLAIM_CLIENT_ID)
            else -> setClaim(CLAIM_CLIENT_ID, value)
        }
    }