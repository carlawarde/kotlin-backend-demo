package io.github.carlawarde.kotlinBackendDemo.core.errors

sealed class RequestError(val code: String, val message: String)

sealed class AuthError(code: String, message: String) : RequestError(code, message) {
    object UnauthorizedError : AuthError("UNAUTHORIZED", "Invalid email or password.")
    object ForbiddenError : AuthError("FORBIDDEN", "Access denied.")
}

data class RequestFieldValidationError(val field: String, val reason: String)

class RequestValidationError(
    message: String,
    val fieldErrors: List<RequestFieldValidationError>
) : RequestError("VALIDATION_ERROR", message)

data class RequestException(val error: RequestError) : RuntimeException(error.message)