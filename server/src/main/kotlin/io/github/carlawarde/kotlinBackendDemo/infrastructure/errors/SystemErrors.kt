package io.github.carlawarde.kotlinBackendDemo.infrastructure.errors

sealed class SystemError(val code: String, val message: String)

object InternalServerError : SystemError("INTERNAL_SERVER_ERROR", "An unexpected error has occurred")
object NotFoundError : SystemError("NOT_FOUND", "Resource not found")