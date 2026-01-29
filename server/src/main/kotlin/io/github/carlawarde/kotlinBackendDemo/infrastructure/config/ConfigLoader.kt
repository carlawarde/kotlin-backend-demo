package io.github.carlawarde.kotlinBackendDemo.infrastructure.config

import io.ktor.server.config.ApplicationConfig

fun loadAppConfig(config: ApplicationConfig): AppConfig =
    with(config) {
        AppConfig(
            port = getIntProperty("ktor.deployment.port"),
            database = run {
                DatabaseConfig(
                    type = getStringProperty("database.type"),
                    host = getStringProperty("database.host"),
                    port = getIntProperty("database.port"),
                    name = getStringProperty("database.name"),
                    user = getStringProperty("database.user"),
                    password = getStringProperty("database.password")
                )
            },
            metrics = run {
                MetricsConfig(
                    service = getStringProperty("metrics.service"),
                    environment = getStringProperty("metrics.environment"),
                    region = getStringProperty("metrics.region"),
                    instance = getStringProperty("metrics.instance")
                )
            }
        )
    }


fun ApplicationConfig.getStringProperty(path: String): String =
    property(path).getString()

fun ApplicationConfig.getIntProperty(path: String): Int =
    property(path).getString().toInt()
