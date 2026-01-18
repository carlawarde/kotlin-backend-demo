package io.github.carlawarde.kotlinBackendDemo.infrastructure.modules

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText

fun Application.configureStatusPages() {
    install(StatusPages) {
        status(HttpStatusCode.NotFound) { call, statusCode ->
            call.respondText(text = "404: Page Not Found", status = statusCode)
        }

        exception<Throwable> { call, cause ->
            call.respondText("${cause.localizedMessage}", status = HttpStatusCode.InternalServerError)
        }
    }
}