package io.github.carlawarde.kotlinBackendDemo.http.routes.user

import io.github.carlawarde.kotlinBackendDemo.core.user.domain.UserFactory
import io.github.carlawarde.kotlinBackendDemo.core.user.dto.PostRegisterUserRequest
import io.github.carlawarde.kotlinBackendDemo.core.user.service.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.userRoutes(userService: UserService) {

    post("/register") {
        val userRequestDto = call.receive<PostRegisterUserRequest>()
        val registeredUser = userService.registerUser(userRequestDto)
        val userResponseDto = UserFactory.toPostRegisterUserResponse(registeredUser)
        call.respond(HttpStatusCode.Created, userResponseDto)
    }

    post("/login") {

    }

    post("/logout") {

    }
}

