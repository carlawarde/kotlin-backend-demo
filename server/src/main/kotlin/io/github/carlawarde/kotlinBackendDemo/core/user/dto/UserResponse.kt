package io.github.carlawarde.kotlinBackendDemo.core.user.dto

import java.time.Instant
import java.util.UUID

data class UserResponse(
    val id: UUID,
    val username: String,
    val email: String,
    val createdAt: Instant,
    val updatedAt: Instant
) {
    constructor(user: domain.user.User) : this(
        id = user.id,
        username = user.username,
        email = user.email,
        createdAt = user.createdAt,
        updatedAt = user.updatedAt
    )
}