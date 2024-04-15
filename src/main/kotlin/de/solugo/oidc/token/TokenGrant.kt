package de.solugo.oidc.token

interface TokenGrant {
    val type: String
    suspend fun process(context: TokenContext)
}