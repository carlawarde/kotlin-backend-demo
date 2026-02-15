package io.github.carlawarde.kotlinBackendDemo.core.di

import io.github.carlawarde.kotlinBackendDemo.core.crypto.BcryptPasswordHasher
import io.github.carlawarde.kotlinBackendDemo.core.crypto.PasswordHasher
import io.github.carlawarde.kotlinBackendDemo.core.di.DomainModules.userModule
import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.types.DatabaseProvider
import org.koin.dsl.module

fun coreModule(databaseProvider: DatabaseProvider) = module {
    single<DatabaseProvider> { databaseProvider }
    single<PasswordHasher> { BcryptPasswordHasher() }
    includes(userModule)
}