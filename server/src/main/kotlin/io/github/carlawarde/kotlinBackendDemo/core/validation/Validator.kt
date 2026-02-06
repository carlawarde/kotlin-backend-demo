package io.github.carlawarde.kotlinBackendDemo.core.validation

import io.github.carlawarde.kotlinBackendDemo.core.errors.DomainException
import io.github.carlawarde.kotlinBackendDemo.core.errors.ValidationError

class Validator<T> {

    private val rules = mutableListOf<(T) -> String?>()

    fun rule(block: (T) -> String?) {
        rules += block
    }

    fun validate(target: T) {
        val errors = rules.mapNotNull { it(target) }
        if (errors.isNotEmpty()) {
            throw DomainException(ValidationError(errors))
        }
    }
}