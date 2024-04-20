package de.solugo.oidc.token.grant

import de.solugo.oidc.model.User
import de.solugo.oidc.token.*
import de.solugo.oidc.util.uuid
import org.springframework.stereotype.Component

@Component
class ClientCredentialsGrant : TokenGrant {
    override val type = "client_credentials"

    override suspend fun process(context: TokenContext) {
        val client = context.client ?: throw TokenException(
            error = TokenError.InvalidRequest,
            description = "Client could not be found",
        )

        context.user = object : User {
            override val id = client.id
            override val username = client.id
        }
    }
}