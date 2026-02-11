package io.github.carlawarde.kotlinBackendDemo.infrastructure.plugins

import io.github.carlawarde.kotlinBackendDemo.core.di.coreModule
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.jetbrains.exposed.v1.jdbc.Database
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureDependencyInjection(database: Database) {
    install(Koin) {
        slf4jLogger()

        modules(coreModule(database))
    }
}