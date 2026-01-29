package io.github.carlawarde.kotlinBackendDemo.infrastructure.metrics

import io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle.AppInfoService
import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry

object HealthMetrics {
    fun build(registry: MeterRegistry, appInfoService: AppInfoService) {
        Gauge.builder("app.liveness") { appInfoService.getLiveness() }
            .description("Application liveness: 1 if the app is running")
            .register(registry)

        Gauge.builder("app.readiness") { appInfoService.getReadiness() }
            .description("Application readiness: 1 if app dependencies are available")
            .register(registry)
    }
}
