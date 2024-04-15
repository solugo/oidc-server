package de.solugo.oidc.token

interface TokenProcessor {

    val step: Step

    suspend fun process(context: TokenContext)

    enum class Step {
        PRE, POST, VALIDATE, CLAIMS
    }
}