package io.github.carlawarde.kotlinBackendDemo.http.routes

import io.github.carlawarde.kotlinBackendDemo.http.routes.user.userRoutes
import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.types.DatabaseStatus
import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.withDatabaseRequired
import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import org.koin.ktor.ext.get

fun Route.coreRoutes(databaseStatus: DatabaseStatus) {

    route("/api") {
        withDatabaseRequired(databaseStatus) {
            userRoutes(get())
        }
    }
}