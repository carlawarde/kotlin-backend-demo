package io.github.carlawarde.kotlinBackendDemo.infrastructure.config

import io.ktor.server.config.ApplicationConfig

fun loadAppConfig(config: ApplicationConfig): AppConfig =
    AppConfig(
        port = config.getPropertyAsInt("ktor.deployment.port"),
        database = DatabaseConfig(
            type = config.getPropertyAsString("database.type"),
            host = config.getPropertyAsString("database.host"),
            port = config.getPropertyAsInt("database.port"),
            name = config.getPropertyAsString("database.name"),
            user = config.getPropertyAsString("database.user"),
            password = config.getPropertyAsString("database.password")
        )
    )

fun ApplicationConfig.getPropertyAsString(path: String): String =
    property(path).getString()

fun ApplicationConfig.getPropertyAsInt(path: String, default: Int? = null): Int =
    propertyOrNull(path)?.getString()?.toInt()
        ?: default
        ?: error("Missing config value: $path")
