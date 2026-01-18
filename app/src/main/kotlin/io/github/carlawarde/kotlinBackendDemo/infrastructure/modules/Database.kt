package io.github.carlawarde.kotlinBackendDemo.infrastructure.modules

import io.github.carlawarde.kotlinBackendDemo.infrastructure.config.AppConfig
import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.DatabaseManager
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopping
import io.micrometer.prometheus.PrometheusMeterRegistry
import org.slf4j.LoggerFactory

fun Application.configureDatabase(config: AppConfig, registry: PrometheusMeterRegistry): DatabaseManager {
    val db = DatabaseManager(config, registry)
    db.start()
    return db
}