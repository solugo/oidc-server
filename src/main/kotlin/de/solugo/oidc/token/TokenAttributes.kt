package de.solugo.oidc.token

import de.solugo.oidc.model.Client
import de.solugo.oidc.model.User
import org.jose4j.jwt.JwtClaims
import org.springframework.util.MultiValueMap

val TokenContext.issuer: String by TokenContext.attribute()
var TokenContext.scopes: Set<String>? by TokenContext.attribute()
var TokenContext.client: Client? by TokenContext.attribute()
var TokenContext.user: User? by TokenContext.attribute()
val TokenContext.commonClaims: JwtClaims by TokenContext.attribute { JwtClaims() }
val TokenContext.idClaims: JwtClaims by TokenContext.attribute { JwtClaims() }
val TokenContext.accessClaims: JwtClaims by TokenContext.attribute { JwtClaims() }
val TokenContext.refreshClaims: JwtClaims by TokenContext.attribute { JwtClaims() }
val TokenContext.parameters: MultiValueMap<String, String> by TokenContext.attribute()
val TokenContext.grantType: String?
    get() = run {
        parameters.getFirst("grant_type")
    }
val TokenContext.clientId: String?
    get() = run {
        parameters.getFirst("client_id")
    }
val TokenContext.clientSecret: String?
    get() = run {
        parameters.getFirst("client_secret")
    }
val TokenContext.subject: String?
    get() = run {
        parameters.getFirst("sub")
    }
val TokenContext.username: String?
    get() = run {
        parameters.getFirst("username")
    }
val TokenContext.password: String?
    get() = run {
        parameters.getFirst("password")
    }
val TokenContext.refreshToken: String?
    get() = run {
        parameters.getFirst("refresh_token")
    }
val TokenContext.code: String?
    get() = run {
        parameters.getFirst("code")
    }