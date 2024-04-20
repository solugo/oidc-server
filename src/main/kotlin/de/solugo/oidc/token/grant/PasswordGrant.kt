package de.solugo.oidc.token.grant

import de.solugo.oidc.model.User
import de.solugo.oidc.token.TokenContext
import de.solugo.oidc.token.TokenGrant
import de.solugo.oidc.token.user
import de.solugo.oidc.token.username
import org.springframework.stereotype.Component

@Component
class PasswordGrant : TokenGrant {
    override val type = "password"

    override suspend fun process(context: TokenContext) {
        val username = context.username ?: return

        context.user = object : User {
            override val id = username
            override val username = username
        }
    }

}