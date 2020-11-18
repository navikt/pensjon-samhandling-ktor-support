package no.nav.pensjon.samhandling.naisserver

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import no.nav.pensjon.samhandling.liveness.isAlive
import no.nav.pensjon.samhandling.liveness.isReady
import no.nav.pensjon.samhandling.metrics.metrics

fun naisServer(serverPort: Int = 8080, aliveCheck: () -> Boolean = { true }, readyCheck: () -> Boolean = { true }) =
    embeddedServer(Netty, createApplicationEnvironment(serverPort, aliveCheck, readyCheck))

private fun createApplicationEnvironment(serverPort: Int = 8080, aliveCheck: () -> Boolean, readyCheck: () -> Boolean) =
    applicationEngineEnvironment {
        connector { port = serverPort }
        module {
            isAlive(aliveCheck)
            isReady(readyCheck)
            metrics()
        }
    }

