package io.github.carlawarde.kotlinBackendDemo.core.errors

import io.github.carlawarde.kotlinBackendDemo.common.errors.AppError

sealed class DomainError(
    internalCode: String,
    userMessage: String,
    logMessage: String
) : AppError(internalCode, userMessage, logMessage)