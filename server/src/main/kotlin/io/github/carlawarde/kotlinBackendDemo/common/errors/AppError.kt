package io.github.carlawarde.kotlinBackendDemo.common.errors

open class AppError(
    val internalCode: String,
    val userMessage: String,
    val logMessage: String
)