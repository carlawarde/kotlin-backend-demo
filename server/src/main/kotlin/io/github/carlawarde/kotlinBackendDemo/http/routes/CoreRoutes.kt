package io.github.carlawarde.kotlinBackendDemo.http.routes

import io.github.carlawarde.kotlinBackendDemo.http.routes.user.userRoutes
import io.ktor.server.routing.Route
import org.koin.ktor.ext.get

fun Route.coreRoutes() {

    userRoutes(get())
}