package no.nav.pensjon.samhandling.naisserver

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

private const val NAIS_URL = "http://localhost:8080"

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class NaisServerTest {
    private val naisServer = naisServer().start()

    @AfterAll
    fun tearDown() {
        naisServer.stop(100, 100)
    }

    @Test
    fun `nais server isAlive endpoint returns 200 OK`() {
        assertEquals(200, getCall("/isAlive").statusCode())
    }

    @Test
    fun `nais server isReady endpoint returns 200 OK`() {
        assertEquals(200, getCall("/isReady").statusCode())
    }

    @Test
    fun `nais server metrics endpoint returns 200 OK`() {
        assertEquals(200, getCall("/metrics").statusCode())
    }

    private fun getCall(path: String): HttpResponse<String> {
        val httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("$NAIS_URL$path"))
                .GET()
                .build()
        return HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString())
    }
}