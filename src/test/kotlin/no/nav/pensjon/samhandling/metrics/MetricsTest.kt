package no.nav.pensjon.samhandling.metrics

import io.ktor.server.netty.*
import io.prometheus.client.Gauge
import org.junit.jupiter.api.AfterAll
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
internal class MetricsTest {
    private val client = HttpClient.newHttpClient()
    private var testApplication: NettyApplicationEngine = testApplication { metrics() }

    @Test
    fun `metric endpoint should return 200`() {
        Assertions.assertEquals(200, client.send(metricRequest(), ofString()).statusCode())
    }

    @Test
    fun `register gauge and read value from endpoint`() {
        val gaugeName = "testgauge"
        val gaugeValue = 10.0

        val testGauge = Gauge
            .build()
            .name(gaugeName)
            .help("tetHelp")
            .register()
        testGauge.set(gaugeValue)

        val response = client.send(metricRequest(), ofString()).body()
        Assertions.assertTrue(response.contains("$gaugeName $gaugeValue"))
    }

    @AfterAll
    fun tearDown() {
        testApplication.stop(100, 1000)
    }

    private fun metricRequest() = HttpRequest.newBuilder()
        .uri(URI.create("$TEST_ENVIRONMENT_HOST$METRICS_PATH"))
        .GET()
        .build()
}
