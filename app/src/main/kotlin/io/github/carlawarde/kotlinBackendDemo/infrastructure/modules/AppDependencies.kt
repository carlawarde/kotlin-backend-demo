package io.github.carlawarde.kotlinBackendDemo.infrastructure.plugins

import io.github.carlawarde.kotlinBackendDemo.app.di.appModule
import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.DatabaseManager
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureAppDependencies(database: DatabaseManager) {
    install(Koin) {
        slf4jLogger()

        modules(appModule(database))
    }
}