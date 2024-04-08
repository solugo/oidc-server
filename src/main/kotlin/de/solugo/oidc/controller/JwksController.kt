package de.solugo.oidc.controller

import de.solugo.oidc.ConfigurationProvider
import de.solugo.oidc.service.JwksService
import org.jose4j.jwk.JsonWebKey
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder

@RestController
@ConditionalOnBean(JwksService::class)
class JwksController(
    private val jwksService: JwksService,
) : ConfigurationProvider {

    override fun provide(builder: UriComponentsBuilder) = mapOf(
        "jwks_uri" to builder.replacePath(".well-known/jwks.json").toUriString(),
    )

    @GetMapping(".well-known/jwks.json")
    fun getJwks() = mapOf(
        "keys" to jwksService.keys.map {
            it.toParams(JsonWebKey.OutputControlLevel.PUBLIC_ONLY)
        }
    )

}