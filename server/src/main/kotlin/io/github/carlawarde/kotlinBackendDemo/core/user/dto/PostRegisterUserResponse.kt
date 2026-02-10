package io.github.carlawarde.kotlinBackendDemo.core.user.dto

import kotlinx.serialization.Serializable

@Serializable
data class PostRegisterUserResponse(
    val id: String,
    val username: String,
    val email: String
)