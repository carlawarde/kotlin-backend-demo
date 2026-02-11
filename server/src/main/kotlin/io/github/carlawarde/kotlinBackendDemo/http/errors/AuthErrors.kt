package io.github.carlawarde.kotlinBackendDemo.http.errors

import io.ktor.http.HttpStatusCode

sealed class AuthError(
    internalCode: String,
    userMessage: String,
    logMessage: String,
    statusCode: HttpStatusCode
) : HttpError(internalCode, userMessage, logMessage, statusCode) {

    object UnauthorizedError : AuthError(
        "AUTH_001",
        "Invalid email or password.",
        "Invalid login attempt made.",
        HttpStatusCode.Unauthorized
    )

    object ForbiddenError : AuthError(
        "AUTH_002",
        "Unauthorized.",
        "Unauthorized attempt made to access a resource.",
        HttpStatusCode.Forbidden
    )

    object AccountLocked : AuthError(
        "AUTH_003",
        "Your account has been locked due to too many failed attempts.",
        "Account locked for security.",
        HttpStatusCode.Unauthorized
    )
}