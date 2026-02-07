package io.github.carlawarde.kotlinBackendDemo.core.di

import io.github.carlawarde.kotlinBackendDemo.core.user.repository.UserRepository
import io.github.carlawarde.kotlinBackendDemo.core.user.repository.UserRepositoryImpl
import io.github.carlawarde.kotlinBackendDemo.core.user.service.UserService
import org.koin.dsl.module

object DomainModules {
    val userModule = module {
        single<UserRepository> { UserRepositoryImpl(get()) }
        single { UserService(get()) }
    }
}