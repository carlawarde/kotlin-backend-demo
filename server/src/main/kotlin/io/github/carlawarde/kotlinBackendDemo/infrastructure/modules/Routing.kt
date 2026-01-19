package io.github.carlawarde.kotlinBackendDemo.infrastructure.modules

import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.DatabaseManager
import io.github.carlawarde.kotlinBackendDemo.infrastructure.monitoring.monitoringRoutes
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.micrometer.prometheus.PrometheusMeterRegistry

fun Application.configureRouting(registry: PrometheusMeterRegistry, database: DatabaseManager) {
    routing {
        monitoringRoutes(registry)
    }
}