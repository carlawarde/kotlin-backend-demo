package io.github.carlawarde.kotlinBackendDemo.core.user.domain

import io.github.carlawarde.kotlinBackendDemo.core.user.dto.CreateUserRequest
import java.util.UUID
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
object UserFactory {
    fun fromDTO(
        dto: CreateUserRequest,
        hashPassword: (String) -> String,
        clock: Clock.System = Clock.System
    ): User {
        val now = clock.now()
        return User(
            id = UUID.randomUUID(),
            username = dto.username,
            email = dto.email.lowercase(),
            passwordHash = hashPassword(dto.password),
            createdAt = now,
            updatedAt = now
        )
    }
}