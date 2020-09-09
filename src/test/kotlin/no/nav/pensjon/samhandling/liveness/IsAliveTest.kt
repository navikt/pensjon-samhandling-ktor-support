package no.nav.pensjon.samhandling.liveness


import io.ktor.server.netty.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import testenvironment.HOST
import testenvironment.PORT
import testenvironment.testApplication
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers.ofString


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class IsAliveTest {
    private val client = HttpClient.newHttpClient()
    private lateinit var testApplication: NettyApplicationEngine

    @Test
    fun `isAlive endpoint should return 200`() {
        testApplication = testApplication { isAlive() }
        testApplication = testApplication.start()

        val response = client.send(isAliveRequest(), ofString())
        assertEquals(200, response.statusCode())
    }

    @Test
    fun `isAlive endpoint should return 500 when callback function returns false`() {
        testApplication = testApplication { isAlive { false } }
        testApplication.start()

        val response = client.send(isAliveRequest(), ofString())
        assertEquals(500, response.statusCode())
    }

    @AfterEach
    fun tearDown() {
        testApplication.stop(100, 1000)
    }

    private fun isAliveRequest() = HttpRequest.newBuilder()
        .uri(URI.create("$HOST:$PORT$IS_ALIVE_PATH"))
        .GET()
        .build()
}
