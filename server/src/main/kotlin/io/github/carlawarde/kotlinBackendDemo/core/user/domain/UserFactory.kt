package io.github.carlawarde.kotlinBackendDemo.core.user.domain

import io.github.carlawarde.kotlinBackendDemo.core.user.dto.PostRegisterUserResponse

object UserFactory {

    fun toPostRegisterUserResponse(
        user: User
    ): PostRegisterUserResponse {
        return PostRegisterUserResponse(
            id = user.id.toString(),
            username = user.username,
            email = user.email
        )
    }
}