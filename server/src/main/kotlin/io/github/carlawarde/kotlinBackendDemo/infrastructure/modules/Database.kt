package io.github.carlawarde.kotlinBackendDemo.infrastructure.modules

import io.github.carlawarde.kotlinBackendDemo.infrastructure.config.AppConfig
import io.github.carlawarde.kotlinBackendDemo.infrastructure.config.DatabaseConfig
import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.DatabaseManager
import io.ktor.server.application.Application
import io.micrometer.prometheus.PrometheusMeterRegistry

fun Application.configureDatabase(config: DatabaseConfig, registry: PrometheusMeterRegistry): DatabaseManager {
    val db = DatabaseManager(config, registry)
    db.start()
    return db
}