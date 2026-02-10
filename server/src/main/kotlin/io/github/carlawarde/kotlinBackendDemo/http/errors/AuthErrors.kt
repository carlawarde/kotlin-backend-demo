package io.github.carlawarde.kotlinBackendDemo.http.errors

import io.ktor.http.HttpStatusCode

sealed class AuthError(
    internalCode: String,
    userMessage: String,
    logMessage: String,
    statusCode: HttpStatusCode
) : HttpError(internalCode, userMessage, logMessage, statusCode) {

    object Unauthorized : AuthError(
        "AUTH_001",
        "Invalid email or password.",
        "Invalid login attempt made.",
        HttpStatusCode.Unauthorized
    )

    object Forbidden : AuthError(
        "AUTH_002",
        "You do not have permission to access this resource.",
        "Unauthorized attempt made to access a resource.",
        HttpStatusCode.Forbidden
    )
}