package io.github.carlawarde.kotlinBackendDemo.core.di

import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.DatabaseManager
import org.koin.dsl.module

fun appModule(database: DatabaseManager) = module {
    single { database }
}