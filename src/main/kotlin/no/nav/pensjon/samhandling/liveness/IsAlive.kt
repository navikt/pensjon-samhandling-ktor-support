package no.nav.pensjon.samhandling.liveness

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

const val IS_ALIVE_PATH = "/isAlive"

fun Application.isAlive(alive: () -> Boolean = { true }) {
    routing {
        get(IS_ALIVE_PATH) {
            call.respondText("", ContentType.Text.Plain, getStatusCode(alive))
        }
    }
}

private fun getStatusCode(ready: () -> Boolean) = if (ready()) HttpStatusCode.OK else HttpStatusCode.InternalServerError