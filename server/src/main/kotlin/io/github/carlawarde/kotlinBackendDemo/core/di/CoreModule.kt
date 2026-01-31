package io.github.carlawarde.kotlinBackendDemo.core.di

import io.github.carlawarde.kotlinBackendDemo.core.user.repository.UserRepository
import io.github.carlawarde.kotlinBackendDemo.core.user.service.UserService
import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.DatabaseManager
import org.koin.dsl.module

val userModule = module {
    single { UserRepository() }
    single { UserService(get()) }
}

fun coreModule(database: DatabaseManager) = module {
    single { database }

    includes(userModule)
}