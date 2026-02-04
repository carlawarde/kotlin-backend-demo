package io.github.carlawarde.kotlinBackendDemo.core.di

import io.github.carlawarde.kotlinBackendDemo.core.user.repository.UserRepository
import io.github.carlawarde.kotlinBackendDemo.core.user.repository.UserRepositoryImpl
import io.github.carlawarde.kotlinBackendDemo.core.user.service.UserService
import org.jetbrains.exposed.v1.jdbc.Database
import org.koin.dsl.module

val userModule = module {
    single<UserRepository> { UserRepositoryImpl(get()) }
    single { UserService(get()) }
}

fun coreModule(database: Database) = module {
    single { database }

    includes(userModule)
}