package io.github.carlawarde.kotlinBackendDemo.core.errors

sealed class DomainError(val message: String)

data class ValidationError(val errors: List<String>) : DomainError(errors.toString()) {
    override fun toString() = errors.joinToString(", ")
}

sealed class UserDomainError(message: String) : DomainError(message) {
    object EmailAlreadyTakenError : UserDomainError("There is already an account with this email.")
    object UsernameAlreadyTakenError : UserDomainError("There is already an account with this username.")
}

data class DomainException(val error: DomainError) : RuntimeException(error.message)