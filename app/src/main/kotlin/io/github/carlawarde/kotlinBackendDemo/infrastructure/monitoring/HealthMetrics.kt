package io.github.carlawarde.kotlinBackendDemo.infrastructure.monitoring

import io.ktor.server.application.Application
import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry

fun Application.configureHealthGauges(registry: MeterRegistry) {
    Gauge.builder("app_liveness") { 1.0 }
        .description("Application liveness: 1 if the app is running")
        .register(registry)
}
