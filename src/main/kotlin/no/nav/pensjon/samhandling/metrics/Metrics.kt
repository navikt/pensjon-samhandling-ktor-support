package no.nav.pensjon.samhandling.metrics

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.metrics.micrometer.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.exporter.common.TextFormat

const val METRICS_PATH = "/metrics"

fun Application.metrics(collectorRegistry: CollectorRegistry = CollectorRegistry.defaultRegistry) {
    install(MicrometerMetrics) { registry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT) }
    routing { metricsRouting(collectorRegistry) }
}

@Suppress("BlockingMethodInNonBlockingContext")
private fun Routing.metricsRouting(collectorRegistry: CollectorRegistry) {
    get(METRICS_PATH) {
        call.respondTextWriter(contentType()) {
            TextFormat.write004(this, collectorRegistry.filteredMetricFamilySamples(metricNames()))
        }
    }
}

private fun contentType() = ContentType.parse(TextFormat.CONTENT_TYPE_004)

private fun PipelineContext<Unit, ApplicationCall>.metricNames() =
    call.request.queryParameters.getAll("name[]")?.toSet() ?: emptySet()