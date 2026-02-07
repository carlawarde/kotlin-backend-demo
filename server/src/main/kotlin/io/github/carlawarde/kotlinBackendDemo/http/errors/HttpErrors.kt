package io.github.carlawarde.kotlinBackendDemo.http.errors

import io.github.carlawarde.kotlinBackendDemo.common.errors.AppError
import io.ktor.http.HttpStatusCode

sealed class HttpError(
    internalCode: String,
    userMessage: String,
    logMessage: String,
    val statusCode: HttpStatusCode
) : AppError(internalCode, userMessage, logMessage)