package io.github.carlawarde.kotlinBackendDemo.infrastructure.modules

import io.github.carlawarde.kotlinBackendDemo.app.di.appModule
import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.DatabaseManager
import io.ktor.server.application.Application
import io.ktor.server.application.install
import kotlinx.coroutines.CoroutineScope
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.installAppDependencies(database: DatabaseManager, appScope: CoroutineScope) {
    install(Koin) {
        slf4jLogger()

        modules(appModule(database, appScope))
    }
}