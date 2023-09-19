package no.nav.pensjon.samhandling.liveness

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

const val IS_READY_PATH = "/isReady"

fun Application.isReady(ready: () -> Boolean = { true }) {
    routing {
        get(IS_READY_PATH) {
            call.respondText("", ContentType.Text.Plain, getStatusCode(ready))
        }
    }
}

private fun getStatusCode(ready: () -> Boolean) = if (ready()) HttpStatusCode.OK else HttpStatusCode.InternalServerError