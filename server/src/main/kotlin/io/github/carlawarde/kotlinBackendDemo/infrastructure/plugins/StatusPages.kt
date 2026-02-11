package io.github.carlawarde.kotlinBackendDemo.infrastructure.plugins

import io.github.carlawarde.kotlinBackendDemo.common.errors.AppError
import io.github.carlawarde.kotlinBackendDemo.common.errors.AppException
import io.github.carlawarde.kotlinBackendDemo.core.errors.DomainError
import io.github.carlawarde.kotlinBackendDemo.http.dto.ErrorResponse
import io.github.carlawarde.kotlinBackendDemo.http.dto.ValidationErrorResponse
import io.github.carlawarde.kotlinBackendDemo.http.errors.HttpError
import io.github.carlawarde.kotlinBackendDemo.core.errors.RequestValidationError
import io.github.carlawarde.kotlinBackendDemo.infrastructure.errors.SystemError
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.path
import io.ktor.server.response.respond
import mu.KLogger
import mu.KotlinLogging

fun AppError.toResponse(): Any = when (this) {
    is RequestValidationError -> ValidationErrorResponse(
        internalCode = internalCode,
        message = userMessage,
        fields = fieldErrors.associate { it.field to it.reasons }
    )
    else -> ErrorResponse(internalCode, userMessage)
}

fun AppError.log(logger: KLogger, context: String) {
    val message = "$context - [$internalCode] $logMessage"
    when (this) {
        is SystemError -> logger.error(message)
        is HttpError -> logger.warn(message)
        is DomainError -> logger.info(message)
        else -> logger.warn(message)
    }
}

fun Application.configureStatusPages() {
    val logger = KotlinLogging.logger {}

    install(StatusPages) {

        exception<AppException> { call, exception ->
            val error = exception.error

            error.log(logger, "Handled AppException on ${call.request.path()}")

            val status = when (error) {
                is SystemError -> HttpStatusCode.InternalServerError
                is HttpError -> error.statusCode
                is DomainError -> HttpStatusCode.BadRequest
                else -> HttpStatusCode.InternalServerError
            }

            call.respond(
                status = status,
                message = error.toResponse()
            )
        }

        status(HttpStatusCode.NotFound) { call, _ ->
            val error = SystemError.NotFoundError
            call.respond(
                status = HttpStatusCode.NotFound,
                message = error.toResponse()
            )
        }

        exception<Throwable> { call, cause ->
            logger.error(cause) {
                "Unhandled application error on ${call.request.path()}"
            }

            val error = SystemError.InternalServerError
            call.respond(
                status = HttpStatusCode.InternalServerError,
                message = error.toResponse()
            )
        }
    }
}