package io.github.carlawarde.kotlinBackendDemo.infrastructure.plugins

import io.github.carlawarde.kotlinBackendDemo.core.api.routes.coreRoutes
import io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle.AppInfoService
import io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle.appInfoRoutes
import io.github.carlawarde.kotlinBackendDemo.infrastructure.metrics.metricsRoutes
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import io.micrometer.prometheus.PrometheusMeterRegistry

fun Application.configureRoutes(appInfoService: AppInfoService, registry: PrometheusMeterRegistry) {
    routing {
        metricsRoutes(registry)
        appInfoRoutes(appInfoService)
        coreRoutes()
    }
}