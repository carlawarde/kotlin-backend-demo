package io.github.carlawarde.kotlinBackendDemo.http.dto

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val internalCode: String,
    val message: String
)

@Serializable
data class ValidationErrorResponse(
    val internalCode: String,
    val message: String,
    val fields: Map<String, List<String>>
)