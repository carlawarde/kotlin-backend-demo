package io.github.carlawarde.kotlinBackendDemo.core.errors

data class FieldValidationError(val field: String, val reasons: List<String>)

class RequestValidationError(
    userMessage: String,
    val fieldErrors: List<FieldValidationError>
): DomainError("VAL_001", userMessage, "Failed validations: $fieldErrors") {}