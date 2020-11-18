package no.nav.pensjon.samhandling.naisserver

import io.ktor.server.netty.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

private const val NAIS_URL = "http://localhost:8080"

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class NaisServerTest {
    private lateinit var naisServer: NettyApplicationEngine

    @AfterEach
    fun afterEach(){
        naisServer.stop(100, 100)
    }

    @Test
    fun `nais server isAlive endpoint returns 200 OK`() {
        naisServer = naisServer().apply { start() }
        assertEquals(200, getCall("/isAlive").statusCode())
    }

    @Test
    fun `nais server isReady endpoint returns 200 OK`() {
        naisServer = naisServer().apply { start() }
        assertEquals(200, getCall("/isReady").statusCode())
    }

    @Test
    fun `nais server metrics endpoint returns 200 OK`() {
        naisServer = naisServer().apply { start() }
        assertEquals(200, getCall("/metrics").statusCode())
    }

    @Test
    fun `nais server isAlive returns 200 when aliveCheck function returns true`() {
        naisServer = naisServer(aliveCheck = {true}).apply { start() }
        assertEquals(200, getCall("/isAlive").statusCode())
    }

    @Test
    fun `nais server isAlive returns 500 when aliveCheck function returns false`() {
        naisServer = naisServer(aliveCheck = {false}).apply { start() }
        assertEquals(500, getCall("/isAlive").statusCode())
    }

    @Test
    fun `nais server isReady returns 200 when readyCheck function returns true`() {
        naisServer = naisServer(readyCheck = {true}).apply { start() }
        assertEquals(200, getCall("/isReady").statusCode())
    }

    @Test
    fun `nais server isReady returns 500 when readyCheck function returns false`() {
        naisServer = naisServer(readyCheck = {false}).apply { start() }
        assertEquals(500, getCall("/isReady").statusCode())
    }


    private fun getCall(path: String): HttpResponse<String> {
        val httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("$NAIS_URL$path"))
                .GET()
                .build()
        return HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString())
    }
}