package io.github.carlawarde.kotlinBackendDemo.core.errors

sealed class AppError(val code: String, val message: String)

sealed class ValidationError(code: String, message: String) : AppError(code, message)

sealed class AuthError(code: String, message: String) : AppError(code, message)
object Unauthenticated : AuthError("UNAUTHENTICATED", "Invalid email or password.")
object Forbidden : AuthError("FORBIDDEN", "Access denied.")

sealed class NotFoundError(code: String, message: String) : AppError(code, message)
object PageNotFound : NotFoundError("PAGE_NOT_FOUND", "Page not found.")
object UserNotFound : NotFoundError("USER_NOT_FOUND", "User not found.")

class AppException(val error: AppError) : RuntimeException(error.message)

