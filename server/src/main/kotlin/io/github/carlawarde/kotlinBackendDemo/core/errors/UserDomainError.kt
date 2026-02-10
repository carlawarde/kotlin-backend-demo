package io.github.carlawarde.kotlinBackendDemo.core.errors

sealed class UserDomainError(
    internalCode: String,
    userMessage: String,
    logMessage: String
) : DomainError(internalCode, userMessage, logMessage) {
    object EmailAlreadyTaken : UserDomainError("USR_001","There is already an account with this email.", "Attempted duplicate email registration.")
    object UsernameAlreadyTaken : UserDomainError("USR_002", "There is already an account with this username.", "Attempted duplicate user registration.")
}