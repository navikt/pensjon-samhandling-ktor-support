package testenvironment

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

private const val PORT = 8081
internal const val TEST_ENVIRONMENT_HOST = "http://localhost:$PORT"

internal fun testApplication(startServer: Boolean = true, testEndpoint: Application.() -> Unit) =
    embeddedServer(Netty, applicationEngine(testEndpoint)).also{
        if(startServer){it.start()}
    }

private fun applicationEngine(testEndpoint: Application.() -> Unit) =
    applicationEngineEnvironment {
        connector { port = PORT }
        module {
            testEndpoint()
        }
    }