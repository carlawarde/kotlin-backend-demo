package io.github.carlawarde.kotlinBackendDemo.core.di

import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.DatabaseManager
import io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle.JobRunner
import kotlinx.coroutines.CoroutineScope
import org.koin.dsl.module

fun appModule(database: DatabaseManager, jobRunner: JobRunner) = module {
    single { database }
    single { jobRunner }
}