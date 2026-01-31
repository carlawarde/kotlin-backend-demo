package io.github.carlawarde.kotlinBackendDemo.http.routes.user

import io.github.carlawarde.kotlinBackendDemo.core.user.dto.CreateUserRequest
import io.github.carlawarde.kotlinBackendDemo.core.user.service.UserService
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.userRoutes(userService: UserService) {

    post("/login") {

    }

    post("/register") {
        val userDTO = call.receive<CreateUserRequest>()
        validateCreateUser(userDTO)
    }

    post("/logout") {

    }
}

fun validateCreateUser(dto: CreateUserRequest) {
    require(dto.username.length in 3..30) { "Username must be 3-30 characters" }
    require(dto.password.length >= 8) { "Password must be at least 8 characters" }
    require(dto.email.matches(".+@.+\\..+".toRegex())) { "Email is invalid" }
}