package io.github.carlawarde.kotlinBackendDemo.setup

import io.github.carlawarde.kotlinBackendDemo.infrastructure.config.loadAppConfig
import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.DatabaseService
import io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle.AppInfoService
import io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle.AppState
import io.github.carlawarde.kotlinBackendDemo.infrastructure.plugins.DatabaseSetup
import io.github.carlawarde.kotlinBackendDemo.infrastructure.plugins.configureSerialization
import io.github.carlawarde.kotlinBackendDemo.infrastructure.plugins.configureStatusPages
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.ApplicationTestBuilder
import io.micrometer.core.instrument.simple.SimpleMeterRegistry


fun ApplicationTestBuilder.buildTestApp(
    startDatabase: Boolean = false,
    configure: Application.(TestDeps) -> Unit
): HttpClient {

    val registry = SimpleMeterRegistry()

    environment {
        config = ApplicationConfig("application-test.conf")
    }

    application {
        val appConfig = loadAppConfig(environment.config)

        val databaseService = if (startDatabase) {
            DatabaseSetup.configure(appConfig.database, registry)
        } else {
            DatabaseService(appConfig.database, registry)
        }

        configureSerialization()
        configureStatusPages()

        configure(
            TestDeps(
                registry = registry,
                databaseService = databaseService
            )
        )
        AppInfoService(databaseService).setStatus(AppState.RUNNING)
    }

    val client: HttpClient = createClient {
        install(ContentNegotiation) { json() }
    }

    return client
}
