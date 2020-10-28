package no.nav.pensjon.samhandling.liveness


import io.ktor.server.netty.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import testenvironment.TEST_ENVIRONMENT_HOST
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
    fun `isAlive returns 200`() {
        testApplication = testApplication { isAlive() }

        assertEquals(200, client.send(isAliveRequest(), ofString()).statusCode())
    }

    @Test
    fun `isAlive returns 200 when callback function returns true`() {
        testApplication = testApplication { isAlive { true } }

        assertEquals(200, client.send(isAliveRequest(), ofString()).statusCode())
    }

    @Test
    fun `isAlive returns 500 when callback function returns false`() {
        testApplication = testApplication { isAlive { false } }

        assertEquals(500, client.send(isAliveRequest(), ofString()).statusCode())
    }

    @AfterEach
    fun tearDown() {
        testApplication.stop(100, 1000)
    }

    private fun isAliveRequest() = HttpRequest.newBuilder()
        .uri(URI.create("$TEST_ENVIRONMENT_HOST$IS_ALIVE_PATH"))
        .GET()
        .build()
}
