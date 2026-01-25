package io.github.carlawarde.kotlinBackendDemo.setup

import io.github.carlawarde.kotlinBackendDemo.infrastructure.config.loadAppConfig
import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.DatabaseManager
import io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle.AppInfoService
import io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle.State
import io.github.carlawarde.kotlinBackendDemo.infrastructure.plugins.configureDatabase
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
    configure: Application.(TestDeps) -> Unit
): HttpClient {

    val registry = SimpleMeterRegistry()

    environment {
        config = ApplicationConfig("application-test.conf")
    }

    application {
        val appConfig = loadAppConfig(environment.config)
        val databaseManager = configureDatabase(appConfig.database, registry)
        configureSerialization()
        configureStatusPages()
        configure(
            TestDeps(
                registry = registry,
                databaseManager = databaseManager
            )
        )
        AppInfoService(databaseManager).setStatus(State.RUNNING)
    }

    val client: HttpClient = createClient {
        install(ContentNegotiation) { json() }
    }

    return client
}
