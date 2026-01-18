package io.github.carlawarde.kotlinBackendDemo.infrastructure.plugins

import io.github.carlawarde.kotlinBackendDemo.infrastructure.config.AppConfig
import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.DatabaseManager
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopping
import io.ktor.server.application.plugin
import io.ktor.server.metrics.micrometer.MicrometerMetrics

fun Application.configureDatabase(config: AppConfig): DatabaseManager {

    val db = DatabaseManager(config, registry)
    db.start()

    this.monitor.subscribe(ApplicationStopping) {
        db.stop()
    }

    return db
}