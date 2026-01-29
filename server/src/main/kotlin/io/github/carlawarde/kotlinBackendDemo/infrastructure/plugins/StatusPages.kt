package io.github.carlawarde.kotlinBackendDemo.infrastructure.plugins

import io.github.carlawarde.kotlinBackendDemo.core.errors.AppException
import io.github.carlawarde.kotlinBackendDemo.core.errors.AuthError
import io.github.carlawarde.kotlinBackendDemo.core.errors.NotFoundError
import io.github.carlawarde.kotlinBackendDemo.core.errors.ValidationError
import io.github.carlawarde.kotlinBackendDemo.infrastructure.errors.SystemError
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.path
import io.ktor.server.response.respondText
import mu.KotlinLogging

fun Application.configureStatusPages() {
    val logger = KotlinLogging.logger {}

    install(StatusPages) {

        exception<AppException> { call, ex ->
            val status = when (ex.error) {
                is ValidationError -> HttpStatusCode.BadRequest
                is NotFoundError -> HttpStatusCode.NotFound
                is AuthError -> HttpStatusCode.Unauthorized
                is SystemError -> HttpStatusCode.InternalServerError
            }

            logger.warn(ex) { "Handled AppException on ${call.request.path()}" }

            call.respond(
                status,
                ErrorResponse(
                    status = status.value,
                    error = status.description,
                    code = ex.error.code,
                    message = ex.error.message,
                    path = call.request.path()
                )
            )
        }

        exception<Throwable> { call, cause ->
            logger.error(cause) { "Unhandled application error on ${call.request.path()}" }

            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(
                    status = 500,
                    error = "Internal Server Error",
                    code = "INTERNAL_SERVER_ERROR",
                    message = "An unexpected error has occured.",
                    path = call.request.path()
                )
            )
        }
    }
}