package no.nav.pensjon.samhandling.naisserver

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import no.nav.pensjon.samhandling.liveness.isAlive
import no.nav.pensjon.samhandling.liveness.isReady
import no.nav.pensjon.samhandling.metrics.metrics

fun naisServer(serverPort: Int = 8080) = embeddedServer(Netty, createApplicationEnvironment(serverPort))

private fun createApplicationEnvironment(serverPort: Int = 8080) =
        applicationEngineEnvironment {
            connector { port = serverPort }
            module {
                isAlive()
                isReady()
                metrics()
            }
        }