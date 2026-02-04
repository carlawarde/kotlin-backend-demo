package io.github.carlawarde.kotlinBackendDemo.core.user.dto

import io.github.carlawarde.kotlinBackendDemo.core.user.domain.User
import java.util.UUID
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class UserResponse(
    val id: UUID,
    val username: String,
    val email: String,
    val createdAt: Instant,
    val updatedAt: Instant
) {
    constructor(user: User) : this(
        id = user.id,
        username = user.username,
        email = user.email,
        createdAt = user.createdAt,
        updatedAt = user.updatedAt
    )
}