package de.solugo.oidc.token

class TokenException(
    val error: TokenError,
    val description: String? = null,
    val uri: String? = null,
    cause: Throwable? = null,
) : Exception(
    "$error${description?.let { ":$it" }}",
    cause,
)