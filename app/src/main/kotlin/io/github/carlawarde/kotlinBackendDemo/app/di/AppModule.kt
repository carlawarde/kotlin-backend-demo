package io.github.carlawarde.kotlinBackendDemo.modules

import io.github.carlawarde.kotlinBackendDemo.infrastructure.config.AppConfig
import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.DatabaseFactory
import io.ktor.server.application.Application
import io.ktor.server.application.plugin
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.micrometer.core.instrument.MeterRegistry
import org.koin.dsl.module

fun appModule(config: ApplicationConfig, app: Application) = module {
    single { AppConfig(config) }

    single<MeterRegistry> {
        app.plugin(MicrometerMetrics).registry
    }

    single { DatabaseFactory(get(), get()) }
}