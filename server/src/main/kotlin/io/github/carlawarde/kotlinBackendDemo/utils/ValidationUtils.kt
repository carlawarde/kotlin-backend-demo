package io.github.carlawarde.kotlinBackendDemo.utils

import io.github.carlawarde.kotlinBackendDemo.core.errors.FieldValidationError
import io.github.carlawarde.kotlinBackendDemo.core.errors.RequestValidationError
import io.konform.validation.ValidationError
import io.konform.validation.messagesAtPath
import io.konform.validation.path.ValidationPath

object ValidationUtils {

    fun createValidationError(errors: List<ValidationError>): RequestValidationError {
        val fieldErrors = mapKonformErrorsToFieldValidationError(errors)
        return RequestValidationError(
            userMessage = "User input did not meet requirements. Please resolve the following issues to proceed.",
            fieldErrors = fieldErrors
        )
    }

    private fun mapKonformErrorsToFieldValidationError(errors: List<ValidationError>): List<FieldValidationError> {
        val uniquePaths = errors.map{ it.path }.toSet()

        return uniquePaths.fold(listOf()) { acc, path ->
            val messages = errors.messagesAtPath(path)
            acc + FieldValidationError(path.dataPath, messages)
        }
    }
}