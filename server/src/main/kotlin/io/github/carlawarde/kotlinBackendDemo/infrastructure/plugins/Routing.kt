package io.github.carlawarde.kotlinBackendDemo.infrastructure.plugins

import io.github.carlawarde.kotlinBackendDemo.http.routes.coreRoutes
import io.github.carlawarde.kotlinBackendDemo.http.routes.swaggerRoute
import io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle.AppInfoService
import io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle.appInfoRoutes
import io.github.carlawarde.kotlinBackendDemo.infrastructure.observability.metricsRoutes
import io.ktor.http.ContentType
import io.ktor.server.application.Application
import io.ktor.server.http.content.files
import io.ktor.server.http.content.resource
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.http.content.staticFiles
import io.ktor.server.http.content.staticResources
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.micrometer.prometheus.PrometheusMeterRegistry
import java.io.File

fun Application.configureRoutes(appInfoService: AppInfoService, registry: PrometheusMeterRegistry) {
    routing {
        metricsRoutes(registry)
        appInfoRoutes(appInfoService)
        coreRoutes()
        swaggerRoute()
    }
}