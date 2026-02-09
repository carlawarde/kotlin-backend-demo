package io.github.carlawarde.kotlinBackendDemo.core.user.dto

import kotlinx.serialization.Serializable
import java.util.UUID
import kotlin.time.ExperimentalTime

@Serializable
data class CreateUserResponse(
    val id: String,
    val username: String,
    val email: String
)