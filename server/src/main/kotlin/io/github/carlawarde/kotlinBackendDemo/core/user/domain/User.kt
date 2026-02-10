package io.github.carlawarde.kotlinBackendDemo.core.user.domain

import java.time.Instant
import java.util.UUID

data class User(
    val id: UUID,
    val username: String,
    val passwordHash: String,
    val email: String,
    val createdAt: Instant,
    val updatedAt: Instant,
)
