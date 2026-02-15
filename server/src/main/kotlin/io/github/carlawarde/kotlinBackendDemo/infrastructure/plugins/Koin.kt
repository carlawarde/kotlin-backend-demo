package io.github.carlawarde.kotlinBackendDemo.infrastructure.plugins

import io.github.carlawarde.kotlinBackendDemo.core.di.coreModule
import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.types.DatabaseProvider
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureDependencyInjection(databaseProvider: DatabaseProvider) {
    install(Koin) {
        slf4jLogger()

        modules(coreModule(databaseProvider))
    }
}