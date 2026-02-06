package io.github.carlawarde.kotlinBackendDemo.http.routes.user

import io.github.carlawarde.kotlinBackendDemo.core.user.dto.CreateUserRequest
import io.github.carlawarde.kotlinBackendDemo.core.user.dto.UserResponse
import io.github.carlawarde.kotlinBackendDemo.core.user.service.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun Route.userRoutes(userService: UserService) {

    post("/register") {
        val userRequestDTO = call.receive<CreateUserRequest>()
        val createdUser = userService.registerUser(userRequestDTO)
        call.respond(HttpStatusCode.Created, UserResponse(createdUser))
    }

    post("/login") {

    }

    post("/logout") {

    }
}

