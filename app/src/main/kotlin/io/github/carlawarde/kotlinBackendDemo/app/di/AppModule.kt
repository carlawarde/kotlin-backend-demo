package io.github.carlawarde.kotlinBackendDemo.app.di

import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.DatabaseManager
import kotlinx.coroutines.CoroutineScope
import org.koin.dsl.module

fun appModule(database: DatabaseManager, appScope: CoroutineScope) = module {
    single { database }
    single<CoroutineScope> { appScope }
}