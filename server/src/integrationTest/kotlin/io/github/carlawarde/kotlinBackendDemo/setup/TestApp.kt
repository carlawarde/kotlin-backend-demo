package io.github.carlawarde.kotlinBackendDemo.setup

import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.DatabaseService
import io.micrometer.core.instrument.MeterRegistry

data class TestDeps(
    val registry: MeterRegistry,
    val databaseService: DatabaseService
)