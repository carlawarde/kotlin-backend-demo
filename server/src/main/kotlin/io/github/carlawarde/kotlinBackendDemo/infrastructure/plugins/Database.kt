package io.github.carlawarde.kotlinBackendDemo.infrastructure.plugins

import io.github.carlawarde.kotlinBackendDemo.infrastructure.config.DatabaseConfig
import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.DatabaseManager
import io.ktor.server.application.Application
import io.micrometer.core.instrument.MeterRegistry

fun Application.configureDatabase(config: DatabaseConfig, registry: MeterRegistry): DatabaseManager {
    val db = DatabaseManager(config, registry)
    db.start()
    return db
}