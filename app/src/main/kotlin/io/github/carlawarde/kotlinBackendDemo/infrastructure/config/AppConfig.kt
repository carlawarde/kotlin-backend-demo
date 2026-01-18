package io.github.carlawarde.kotlinBackendDemo.infrastructure.config

import io.ktor.server.config.ApplicationConfig

class AppConfig(config: ApplicationConfig) {
    val port = config.property("ktor.deployment.port").getString().trim().toInt()

    val dbUrl = config.property("database.url").getString().trim()
    val dbUser = config.property("database.user").getString().trim()
    val dbPassword = config.property("database.password").getString().trim()
}