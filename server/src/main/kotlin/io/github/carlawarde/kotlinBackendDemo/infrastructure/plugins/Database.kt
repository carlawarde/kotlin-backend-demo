package io.github.carlawarde.kotlinBackendDemo.infrastructure.plugins

import io.github.carlawarde.kotlinBackendDemo.infrastructure.config.DatabaseConfig
import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.DatabaseService
import io.micrometer.core.instrument.MeterRegistry

object DatabaseSetup {
    fun configure(config: DatabaseConfig, registry: MeterRegistry): DatabaseService {
        val db = DatabaseService(config, registry)
        db.start()
        return db
    }
}
