package io.github.carlawarde.kotlinBackendDemo.core.user.service

import io.github.carlawarde.kotlinBackendDemo.common.errors.AppException
import io.github.carlawarde.kotlinBackendDemo.core.crypto.PasswordHasher
import io.github.carlawarde.kotlinBackendDemo.core.user.domain.User
import io.github.carlawarde.kotlinBackendDemo.core.user.dto.CreateUserRequest
import io.github.carlawarde.kotlinBackendDemo.core.user.repository.UserRepository
import io.github.carlawarde.kotlinBackendDemo.core.user.validation.CreateUserRequestValidation
import io.github.carlawarde.kotlinBackendDemo.core.errors.RequestValidationError
import io.github.carlawarde.kotlinBackendDemo.logger
import io.github.carlawarde.kotlinBackendDemo.utils.ValidationUtils
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class UserService(private val userRepository: UserRepository, private val passwordHasher: PasswordHasher) {

    suspend fun registerUser(userRequest: CreateUserRequest): User {
        val result = CreateUserRequestValidation.validate(userRequest)
        if (!result.isValid) {
            val validationError = ValidationUtils.createValidationError("User input did not meet requirements. Please resolve the following issues to proceed.", result.errors)
            logger.warn(validationError.logMessage)
            throw AppException(validationError)
        }

        val username = userRequest.username
        val email = userRequest.email
        val passwordHash = passwordHasher.hash(userRequest.password)
        return userRepository.create(username, email, passwordHash)
    }
}