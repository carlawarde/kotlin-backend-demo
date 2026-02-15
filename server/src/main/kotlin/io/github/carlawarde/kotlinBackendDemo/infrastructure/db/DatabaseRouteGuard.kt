package io.github.carlawarde.kotlinBackendDemo.infrastructure.db

import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.types.DatabaseStatus
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.createRouteScopedPlugin
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.route

class RequireDatabaseReadyConfig {
    lateinit var dbStatus: DatabaseStatus
}

val RequireDatabaseReady = createRouteScopedPlugin("RequireDatabaseReady", ::RequireDatabaseReadyConfig) {
    val dbStatus = pluginConfig.dbStatus

    onCall { call ->
        if (!dbStatus.isReady()) {
            call.respond(HttpStatusCode.ServiceUnavailable, "Service unavailable, please try again later.")
        }
    }
}

fun Route.withDatabaseRequired(
    dbStatus: DatabaseStatus,
    block: Route.() -> Unit
) {
    route("") {
        install(RequireDatabaseReady) {
            this.dbStatus = dbStatus
        }
        block()
    }
}
