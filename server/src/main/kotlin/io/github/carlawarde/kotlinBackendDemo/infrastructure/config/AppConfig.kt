package io.github.carlawarde.kotlinBackendDemo.infrastructure.config


data class AppConfig(
    val port: Int,
    val database: DatabaseConfig
)