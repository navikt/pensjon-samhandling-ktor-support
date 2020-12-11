package no.nav.pensjon.samhandling.metrics

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.metrics.micrometer.*
import io.ktor.response.*
import io.ktor.routing.*
import io.micrometer.core.instrument.Clock
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.exporter.common.TextFormat

const val METRICS_PATH = "/metrics"

fun Application.metrics(
    meterRegistry: PrometheusMeterRegistry = PrometheusMeterRegistry(
        PrometheusConfig.DEFAULT,
        CollectorRegistry.defaultRegistry,
        Clock.SYSTEM
    )
) {
    install(MicrometerMetrics) {
        registry = meterRegistry
        JvmMemoryMetrics().bindTo(registry)
        ProcessorMetrics().bindTo(registry)
        JvmThreadMetrics().bindTo(registry)
    }
    routing { metricsRouting(meterRegistry) }
}

@Suppress("BlockingMethodInNonBlockingContext")
private fun Routing.metricsRouting(collectorRegistry: PrometheusMeterRegistry) {
    get(METRICS_PATH) {
        call.respondTextWriter(ContentType.parse(TextFormat.CONTENT_TYPE_004)) {
            TextFormat.write004(this, collectorRegistry.prometheusRegistry.metricFamilySamples())
        }
    }
}