package de.solugo.oidc.controller

import IntegrationTest
import com.fasterxml.jackson.databind.node.ObjectNode
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class TokenControllerTest : IntegrationTest() {

    @Test
    fun `Get openid configuration`() = runTest {
        rest.get(".well-known/openid-configuration").apply {
            status shouldBe HttpStatusCode.OK
            body<ObjectNode>().apply {
                at("/issuer").textValue() shouldBe "https://my_issuer/"
            }
        }
    }


    @Test
    fun `Create token using password grant`() = runTest {
        val parameters = parametersOf(
            "grant_type" to listOf("password"),
            "client_id" to listOf("client_test"),
            "username" to listOf("test"),
            "scope" to listOf("openid offline_access"),
        )

        rest.post("token") {
            setBody(FormDataContent(parameters))
        }.apply {
            status shouldBe HttpStatusCode.OK
            body<ObjectNode>().apply {

                at("/token_type").textValue() shouldBe "Bearer"
                at("/expires_in").numberValue() shouldBe 3600
                at("/access_token").textValue() shouldNotBe null
                at("/id_token").textValue() shouldNotBe null
                at("/refresh_token").textValue() shouldNotBe null
            }
        }
    }

    @Test
    fun `Create token using client credentials grant`() = runTest {
        val parameters = parametersOf(
            "grant_type" to listOf("client_credentials"),
            "client_id" to listOf("client_test"),
            "scope" to listOf("custom"),
        )

        rest.post("token") {
            setBody(FormDataContent(parameters))
        }.apply {
            status shouldBe HttpStatusCode.OK
            body<ObjectNode>().apply {
                at("/token_type").textValue() shouldBe "Bearer"
                at("/expires_in").numberValue() shouldBe 3600
                at("/access_token").textValue() shouldNotBe null
                at("/id_token").textValue() shouldBe null
                at("/refresh_token").textValue() shouldBe null
            }
        }
    }

    @Test
    fun `Create token using refresh token`() = runTest {
        val passwordParameters = parametersOf(
            "grant_type" to listOf("password"),
            "client_id" to listOf("client_test"),
            "username" to listOf("test"),
            "scope" to listOf("openid offline_access"),
        )

        val refreshToken = rest.post("token") {
            setBody(FormDataContent(passwordParameters))
        }.run {
            body<ObjectNode>().at("/refresh_token").textValue()
        }

        val refreshParameters = parametersOf(
            "grant_type" to listOf("refresh_token"),
            "client_id" to listOf("client_test"),
            "refresh_token" to listOf(refreshToken),
        )

        rest.post("token") {
            setBody(FormDataContent(refreshParameters))
        }.apply {
            status shouldBe HttpStatusCode.OK
            body<ObjectNode>().apply {
                at("/token_type").textValue() shouldBe "Bearer"
                at("/expires_in").numberValue() shouldBe 3600
                at("/access_token").textValue() shouldNotBe null
                at("/id_token").textValue() shouldNotBe null
                at("/refresh_token").textValue() shouldNotBe null
            }
        }
    }
}
