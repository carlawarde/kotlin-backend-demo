package io.github.carlawarde.kotlinBackendDemo.core.user.service

import io.github.carlawarde.kotlinBackendDemo.core.user.domain.User
import io.github.carlawarde.kotlinBackendDemo.core.user.dto.CreateUserRequest
import io.github.carlawarde.kotlinBackendDemo.core.user.repository.UserRepository
import io.github.carlawarde.kotlinBackendDemo.core.user.domain.UserFactory
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class UserService(private val userRepository: UserRepository) {

    suspend fun registerUser(userRequest: CreateUserRequest): User {
        val user = UserFactory.fromCreateRequestDto(userRequest, { hashPassword(it) })
        return userRepository.create(user)
    }

    private fun hashPassword(raw: String): String {
        return "hashed-$raw"
    }
}