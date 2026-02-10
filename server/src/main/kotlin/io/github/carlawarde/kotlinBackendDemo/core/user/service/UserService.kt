package io.github.carlawarde.kotlinBackendDemo.core.user.service

import io.github.carlawarde.kotlinBackendDemo.common.errors.AppException
import io.github.carlawarde.kotlinBackendDemo.core.crypto.PasswordHasher
import io.github.carlawarde.kotlinBackendDemo.core.user.domain.User
import io.github.carlawarde.kotlinBackendDemo.core.user.dto.PostRegisterUserRequest
import io.github.carlawarde.kotlinBackendDemo.core.user.repository.UserRepository
import io.github.carlawarde.kotlinBackendDemo.core.user.validation.PostRegisterUserRequestValidation
import io.github.carlawarde.kotlinBackendDemo.logger
import io.github.carlawarde.kotlinBackendDemo.utils.ValidationUtils

class UserService(private val userRepository: UserRepository, private val passwordHasher: PasswordHasher) {

    suspend fun registerUser(userRequest: PostRegisterUserRequest): User {
        val result = PostRegisterUserRequestValidation.validate(userRequest)
        if (!result.isValid) {
            val validationError = ValidationUtils.createValidationError( result.errors)
            logger.warn(validationError.logMessage)
            throw AppException(validationError)
        }

        val username = userRequest.username
        val email = userRequest.email
        val passwordHash = passwordHasher.hash(userRequest.password)
        return userRepository.create(username, email, passwordHash)
    }
}