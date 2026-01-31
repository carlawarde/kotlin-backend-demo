package io.github.carlawarde.kotlinBackendDemo.infrastructure.http

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val code: String,
    val message: String
)

@Serializable
data class ValidationErrorResponse(
    val code: String,
    val message: String,
    val fields: Map<String, String>
)