package de.solugo.oidc.util

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

fun badRequest(message: String): Nothing = throw ResponseStatusException(HttpStatus.BAD_REQUEST, message)