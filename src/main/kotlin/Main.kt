@file:JvmName("OidcServer")

import de.solugo.oidc.Server
import org.springframework.boot.runApplication


fun main(args: Array<String>) {
    runApplication<Server>(*args)
}