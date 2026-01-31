package io.github.carlawarde.kotlinBackendDemo.core.user.domain

import java.util.UUID
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class User(
    val id: UUID,
    val username: String,
    val passwordHash: String,
    val email: String,
    val createdAt: Instant,
    val updatedAt: Instant
)
