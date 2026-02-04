package io.github.carlawarde.kotlinBackendDemo.core.errors

sealed class CoreError(val code: String, val message: String)

sealed class AuthError(code: String, message: String) : CoreError(code, message)
object UnauthorizedError : AuthError("UNAUTHORIZED", "Invalid email or password")
object ForbiddenError : AuthError("FORBIDDEN", "Access denied")

data class FieldValidationError(val field: String, val reason: String)

class ValidationError(
    message: String,
    val fieldErrors: List<FieldValidationError>
) : CoreError("VALIDATION_ERROR", message)


class AppException(val error: CoreError) : RuntimeException(error.message)