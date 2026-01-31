package io.github.carlawarde.kotlinBackendDemo.infrastructure.plugins

import io.github.carlawarde.kotlinBackendDemo.core.errors.*
import io.github.carlawarde.kotlinBackendDemo.infrastructure.errors.InternalServerError
import io.github.carlawarde.kotlinBackendDemo.infrastructure.errors.NotFoundError
import io.github.carlawarde.kotlinBackendDemo.http.dto.ErrorResponse
import io.github.carlawarde.kotlinBackendDemo.http.dto.ValidationErrorResponse
import io.github.carlawarde.kotlinBackendDemo.utils.HttpUtils.toHttpStatus
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.path
import io.ktor.server.response.respond
import mu.KotlinLogging

fun Application.configureStatusPages() {
    val logger = KotlinLogging.logger {}

    install(StatusPages) {

        exception<AppException> { call, exception ->
            val error = exception.error
            logger.warn(exception) {
                "Handled AppException [${error.code}] on ${call.request.path()}"
            }

            val errorResponse = when (error) {
                is ValidationError -> ValidationErrorResponse(
                    code = error.code,
                    message = error.message,
                    fields = error.fieldErrors.associate { it.field to it.reason }
                )

                else -> ErrorResponse(
                    code = error.code,
                    message = error.message
                )
            }

            call.respond(error.toHttpStatus(), errorResponse)
        }

        status(HttpStatusCode.NotFound) { call, _ ->
            call.respond(
                HttpStatusCode.NotFound,
                ErrorResponse(
                    code = NotFoundError.code,
                    message = NotFoundError.message
                )
            )
        }

        exception<Throwable> { call, cause ->
            logger.error(cause) { "Unhandled application error on ${call.request.path()}" }

            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(
                    code = InternalServerError.code,
                    message = InternalServerError.message
                )
            )
        }
    }
}