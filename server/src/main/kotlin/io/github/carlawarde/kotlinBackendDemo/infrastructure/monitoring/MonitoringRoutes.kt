package io.github.carlawarde.kotlinBackendDemo.infrastructure.monitoring

import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.micrometer.prometheus.PrometheusMeterRegistry

fun Route.monitoringRoutes(registry: PrometheusMeterRegistry) {

    get("/metrics") {
        call.respond(registry.scrape())
    }

    get("/health/live") {
        call.respondText("OK")
    }
}