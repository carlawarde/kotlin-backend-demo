package io.github.carlawarde.kotlinBackendDemo.http.errors

import io.github.carlawarde.kotlinBackendDemo.common.errors.AppError
import io.ktor.http.HttpStatusCode

sealed class HttpError(
    internalCode: String,
    userMessage: String,
    logMessage: String,
    val statusCode: HttpStatusCode
) : AppError(internalCode, userMessage, logMessage) {
    object InvalidRequestBody : HttpError(
        internalCode = "HTTP_001",
        userMessage = "Request body is malformed or missing required fields.",
        logMessage = "Invalid request body received.",
        statusCode = HttpStatusCode.BadRequest
    )
}

