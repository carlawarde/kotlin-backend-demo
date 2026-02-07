package io.github.carlawarde.kotlinBackendDemo.common.errors

class AppException(
    val error: AppError,
    cause: Throwable? = null
) : RuntimeException(error.logMessage, cause)