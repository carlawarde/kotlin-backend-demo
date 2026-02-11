package io.github.carlawarde.kotlinBackendDemo.http.routes

import io.ktor.server.http.content.staticResources
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.swaggerRoute() {
    staticResources("/swagger", "swagger")
    staticResources("/docs", "docs")

    get("/swagger.html") {
        call.respondRedirect("/swagger/swagger-ui.html")
    }
}