package io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.appInfoRoutes(appInfoService: AppInfoService) {

    route("/health") {
        get("/live") {
            when(appInfoService.getLiveness()) {
                1.0 -> call.respond(HttpStatusCode.OK, "OK")
                0.0 -> call.respond(HttpStatusCode.ServiceUnavailable, "NOT OK")
            }
        }

        get("/ready") {
            when(appInfoService.getReadiness()) {
                1.0 -> call.respond(HttpStatusCode.OK, "READY")
                0.0 -> call.respond(HttpStatusCode.ServiceUnavailable, "NOT READY")
            }
        }

        get("/status") {
            call.respond(appInfoService.getStatus())
        }
    }
}