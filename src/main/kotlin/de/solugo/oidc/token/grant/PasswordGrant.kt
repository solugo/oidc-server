package de.solugo.oidc.token.grant

import de.solugo.oidc.model.User
import de.solugo.oidc.token.*
import de.solugo.oidc.util.uuid
import org.springframework.stereotype.Component

@Component
class PasswordGrant : TokenGrant {
    override val type = "password"

    override suspend fun process(context: TokenContext) {
        val username = context.username ?: return

        context.user = object : User {
            override val id = context.subject ?: uuid(username)
            override val username = username
        }
    }

}