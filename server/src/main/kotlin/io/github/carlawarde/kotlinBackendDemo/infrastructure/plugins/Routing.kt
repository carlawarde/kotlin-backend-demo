package io.github.carlawarde.kotlinBackendDemo.infrastructure.plugins

import io.github.carlawarde.kotlinBackendDemo.http.routes.coreRoutes
import io.github.carlawarde.kotlinBackendDemo.http.routes.swaggerRoute
import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.types.DatabaseStatus
import io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle.AppInfoService
import io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle.appInfoRoutes
import io.github.carlawarde.kotlinBackendDemo.infrastructure.observability.metricsRoutes
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import io.micrometer.prometheus.PrometheusMeterRegistry

fun Application.configureRoutes(appInfoService: AppInfoService, registry: PrometheusMeterRegistry, databaseStatus: DatabaseStatus) {
    routing {
        metricsRoutes(registry)
        appInfoRoutes(appInfoService)
        swaggerRoute()
        coreRoutes(databaseStatus)
    }
}

