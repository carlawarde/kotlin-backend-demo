package io.github.carlawarde.kotlinBackendDemo.http.routes

import io.github.carlawarde.kotlinBackendDemo.core.user.service.UserService
import io.github.carlawarde.kotlinBackendDemo.http.routes.user.userRoutes
import io.ktor.server.routing.Route
import org.koin.ktor.ext.inject

fun Route.coreRoutes() {
    val userService: UserService by inject()

    userRoutes(userService)
}