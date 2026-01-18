package io.github.carlawarde.kotlinBackendDemo.config

import io.ktor.server.config.ApplicationConfig

class AppConfig(config: ApplicationConfig) {
    val port = config.propertyOrNull("ktor.deployment.port")?.getString()?.toInt() ?: 8080

    val dbDriver = config.property("database.driver-name").getString()
    val dbUrl = config.property("database.jdbc-url").getString()
    val dbUser = config.property("database.user").getString()
    val dbPassword = config.property("database.password").getString()
}