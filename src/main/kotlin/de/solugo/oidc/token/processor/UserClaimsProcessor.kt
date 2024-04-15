package de.solugo.oidc.token.processor

import de.solugo.oidc.token.*
import de.solugo.oidc.util.preferredUsername
import org.springframework.stereotype.Component

@Component
class UserClaimsProcessor : TokenProcessor {

    override val step =  TokenProcessor.Step.CLAIMS

    override suspend fun process(context: TokenContext) {
        val user = context.user ?: return

        context.commonClaims.apply {
            subject = user.id
            preferredUsername = user.username
        }
    }

}