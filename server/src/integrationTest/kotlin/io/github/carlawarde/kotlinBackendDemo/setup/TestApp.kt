package io.github.carlawarde.kotlinBackendDemo.setup

import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.DatabaseManager
import io.micrometer.core.instrument.MeterRegistry

data class TestDeps(
    val registry: MeterRegistry,
    val databaseManager: DatabaseManager
)