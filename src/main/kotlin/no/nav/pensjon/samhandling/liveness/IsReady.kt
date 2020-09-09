package no.nav.pensjon.samhandling.liveness

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

const val IS_READY_PATH = "/isReady"

internal fun Application.isReady(ready: () -> Boolean = { true }) {
    routing {
        get(IS_READY_PATH) {
            call.respondText("", ContentType.Text.Plain, getStatusCode(ready))
        }
    }
}

private fun getStatusCode(ready: () -> Boolean) = if (ready()) HttpStatusCode.OK else HttpStatusCode.InternalServerError