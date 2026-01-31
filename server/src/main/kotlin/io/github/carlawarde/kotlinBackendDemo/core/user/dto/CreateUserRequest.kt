package io.github.carlawarde.kotlinBackendDemo.core.user.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequest(val username: String, val email: String, val password: String)