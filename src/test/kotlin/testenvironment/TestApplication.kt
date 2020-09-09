package testenvironment

import io.ktor.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

internal const val PORT = 8081
internal const val HOST = "http://localhost"

internal fun testApplication(testEndpoint: Application.() -> Unit) =
    embeddedServer(Netty, applicationEngine(testEndpoint))

private fun applicationEngine(testEndpoint: Application.() -> Unit) =
    applicationEngineEnvironment {
        connector { port = PORT }
        module {
            testEndpoint()
        }
    }