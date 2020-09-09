package no.nav.pensjon.samhandling.metrics

import io.ktor.server.netty.*
import org.junit.jupiter.api.*
import testenvironment.HOST
import testenvironment.PORT
import testenvironment.testApplication
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers.ofString

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class MetricsTest {
    private val client = HttpClient.newHttpClient()
    private var testApplication: NettyApplicationEngine = testApplication { metrics() }

    @Test
    fun `metric endpoint should return 200`() {
        val response = client.send(metricRequest(), ofString())
        Assertions.assertEquals(200, response.statusCode())
    }

    @BeforeAll
    fun init() {
        testApplication.start()
    }

    @AfterAll
    fun tearDown() {
        testApplication.stop(100, 1000)
    }

    private fun metricRequest() = HttpRequest.newBuilder()
        .uri(URI.create("$HOST:$PORT$METRICS_PATH"))
        .GET()
        .build()
}
