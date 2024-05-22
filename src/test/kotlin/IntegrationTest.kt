import de.solugo.oidc.Server
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = [Server::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class IntegrationTest {

    @Autowired
    private lateinit var environment: Environment

    protected val rest = HttpClient {
        install(DefaultRequest) {
            url("http://localhost:${environment["local.server.port"]}")
            contentType(ContentType.Application.Json)
        }
        install(ContentNegotiation) {
            jackson()
        }
    }

    companion object {
        @JvmStatic
        @DynamicPropertySource
        @Suppress("unused")
        fun configure(registry: DynamicPropertyRegistry) {
            registry.add("server.publicUrl") { "https://my_issuer" }
        }
    }

}
