package de.solugo.oidc.token.processor

import de.solugo.oidc.model.Client
import de.solugo.oidc.token.*
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class ClientResolutionProcessor : TokenProcessor {

    override val step = TokenProcessor.Step.PRE

    override suspend fun process(context: TokenContext) {
        val clientId = context.clientId ?: throw TokenException(
            error = TokenError.InvalidRequest,
            description = "Request is missing client_id parameter",
        )

        context.client = object : Client {
            override val id = clientId
            override val allowedGrants: Set<String> = setOf("*")
            override val accessTokenLifetime: Duration? = Duration.ofHours(1)
        }
    }

}