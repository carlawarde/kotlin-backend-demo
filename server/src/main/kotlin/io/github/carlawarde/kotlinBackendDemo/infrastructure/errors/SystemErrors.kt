package io.github.carlawarde.kotlinBackendDemo.infrastructure.errors

sealed class SystemError(
    code: String,
    message: String
)

object ExternalServiceFailure : SystemError(
    "EXTERNAL_SERVICE_FAILURE",
    "External service failed or timed out."
)

object UnexpectedError : SystemError(
    "UNEXPECTED_ERROR",
    "An unexpected error has occurred."
)
