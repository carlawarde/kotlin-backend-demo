package io.github.carlawarde.kotlinBackendDemo.core.di

import io.github.carlawarde.kotlinBackendDemo.core.crypto.BcryptPasswordHasher
import io.github.carlawarde.kotlinBackendDemo.core.crypto.PasswordHasher
import io.github.carlawarde.kotlinBackendDemo.core.di.DomainModules.userModule
import org.jetbrains.exposed.v1.jdbc.Database
import org.koin.dsl.module

fun coreModule(database: Database) = module {
    single { database }
    single<PasswordHasher> { BcryptPasswordHasher() }
    includes(userModule)
}