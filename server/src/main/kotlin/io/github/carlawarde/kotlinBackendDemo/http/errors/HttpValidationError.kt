package io.github.carlawarde.kotlinBackendDemo.http.errors

import io.ktor.http.HttpStatusCode

data class HttpFieldValidationError(val field: String, val reason: String)

class HttpValidationError(
    userMessage: String,
    logMessage: String,
    statusCode: HttpStatusCode,
    val fieldErrors: List<HttpFieldValidationError>
) : HttpError("HVAL_001", userMessage, logMessage, statusCode)