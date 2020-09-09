package no.nav.pensjon.samhandling.liveness

import io.ktor.server.netty.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
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
internal class IsReadyTest {
    private val client = HttpClient.newHttpClient()
    private lateinit var testApplication: NettyApplicationEngine

    @Test
    fun `isReady endpoint should return 200`() {
        testApplication = testApplication { isReady() }
        testApplication = testApplication.start()

        val response = client.send(isReadyRequest(), ofString())
        Assertions.assertEquals(200, response.statusCode())
    }

    @Test
    fun `isReady should return 500 when callback function returns false`() {
        testApplication = testApplication { isReady { false } }
        testApplication.start()

        val response = client.send(isReadyRequest(), ofString())
        Assertions.assertEquals(500, response.statusCode())
    }

    @AfterEach
    fun tearDown() {
        testApplication.stop(100, 1000)
    }

    private fun isReadyRequest() = HttpRequest.newBuilder()
        .uri(URI.create("$HOST:$PORT$IS_READY_PATH"))
        .GET()
        .build()
}
