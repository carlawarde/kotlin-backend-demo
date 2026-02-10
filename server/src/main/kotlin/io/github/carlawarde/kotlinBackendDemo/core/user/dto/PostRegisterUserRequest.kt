package io.github.carlawarde.kotlinBackendDemo.core.user.dto

import kotlinx.serialization.Serializable

@Serializable
data class PostRegisterUserRequest(val username: String, val email: String, val password: String)