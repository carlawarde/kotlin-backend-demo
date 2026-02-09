package io.github.carlawarde.kotlinBackendDemo.core.user.domain

import io.github.carlawarde.kotlinBackendDemo.core.user.dto.CreateUserRequest
import io.github.carlawarde.kotlinBackendDemo.core.user.dto.CreateUserResponse
import io.github.carlawarde.kotlinBackendDemo.core.user.validation.CreateUserRequestValidator
import java.util.UUID
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
object UserFactory {
    fun fromCreateRequestDto(
        dto: CreateUserRequest,
        hashPassword: (String) -> String,
    ): User {
        CreateUserRequestValidator.validate(dto)

        return User(
            id = UUID.randomUUID(),
            username = dto.username,
            email = dto.email.lowercase(),
            passwordHash = hashPassword(dto.password)
        )
    }

    fun toCreateUserResponseDto(
        user: User
    ): CreateUserResponse {
        return CreateUserResponse(
            id = user.id.toString(),
            username = user.username,
            email = user.email
        )
    }
}