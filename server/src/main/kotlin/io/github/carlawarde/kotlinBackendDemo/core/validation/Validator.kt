package io.github.carlawarde.kotlinBackendDemo.core.validation

import io.github.carlawarde.kotlinBackendDemo.common.errors.AppException
import io.github.carlawarde.kotlinBackendDemo.core.errors.ValidationError
import mu.KotlinLogging

class Validator<T>(private val targetName: String? = null) {
    private val logger = KotlinLogging.logger {}
    private val rules = mutableListOf<(T) -> String?>()

    fun rule(block: (T) -> String?) {
        rules += block
    }

    fun validate(target: T) {
        val errors = rules.mapNotNull { it(target) }
        if (errors.isNotEmpty()) {
            val nameForLog = targetName ?: target!!::class.simpleName ?: "UnknownTarget"
            val validationError = ValidationError(nameForLog, errors)
            logger.warn { validationError.logMessage }
            throw AppException(validationError)
        }
    }
}

class Validation(val field: String, val message: String)