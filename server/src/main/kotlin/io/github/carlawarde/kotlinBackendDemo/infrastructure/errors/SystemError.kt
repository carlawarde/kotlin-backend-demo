package io.github.carlawarde.kotlinBackendDemo.infrastructure.errors

import io.github.carlawarde.kotlinBackendDemo.common.errors.AppError

sealed class SystemError(
    internalCode: String,
    userMessage: String,
    logMessage: String
): AppError(internalCode, userMessage, logMessage) {
    object InternalServerError : SystemError("SYS_001", "An unexpected error has occurred.", "InternalServerError has occurred.")
    object NotFoundError : SystemError("SYS_002", "Resource not found.", "NotFoundError has occurred.")
}

