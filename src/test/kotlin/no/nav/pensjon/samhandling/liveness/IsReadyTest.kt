package no.nav.pensjon.samhandling.liveness

import io.ktor.server.netty.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import testenvironment.TEST_ENVIRONMENT_HOST
import testenvironment.testApplication
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers.ofString


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class IsReadyTest {
    private val client = HttpClient.newHttpClient()
    private lateinit var testApplication: NettyApplicationEngine

    @Test
    fun `isReady endpoint returns 200`() {
        testApplication = testApplication { isReady() }

        Assertions.assertEquals(200, client.send(isReadyRequest(), ofString()).statusCode())
    }

    @Test
    fun `isReady returns 200 when callback function returns true`() {
        testApplication = testApplication { isReady { true } }

        Assertions.assertEquals(200, client.send(isReadyRequest(), ofString()).statusCode())
    }

    @Test
    fun `isReady returns 500 when callback function returns false`() {
        testApplication = testApplication { isReady { false } }

        Assertions.assertEquals(500, client.send(isReadyRequest(), ofString()).statusCode())
    }

    @AfterEach
    fun tearDown() {
        testApplication.stop(100, 1000)
    }

    private fun isReadyRequest() = HttpRequest.newBuilder()
        .uri(URI.create("$TEST_ENVIRONMENT_HOST$IS_READY_PATH"))
        .GET()
        .build()
}
