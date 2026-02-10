package io.github.carlawarde.kotlinBackendDemo.core.user.domain

import io.github.carlawarde.kotlinBackendDemo.core.user.dto.CreateUserResponse
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
object UserFactory {

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