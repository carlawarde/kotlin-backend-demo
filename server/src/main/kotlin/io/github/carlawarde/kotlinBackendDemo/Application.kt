package io.github.carlawarde.kotlinBackendDemo


import io.github.carlawarde.kotlinBackendDemo.infrastructure.config.loadAppConfig
import io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle.AppInfoService
import io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle.State
import io.github.carlawarde.kotlinBackendDemo.infrastructure.monitoring.HealthMetrics
import io.github.carlawarde.kotlinBackendDemo.infrastructure.plugins.configureDependencyInjection
import io.github.carlawarde.kotlinBackendDemo.infrastructure.plugins.configureDatabase
import io.github.carlawarde.kotlinBackendDemo.infrastructure.plugins.configureMonitoring
import io.github.carlawarde.kotlinBackendDemo.infrastructure.plugins.configureRoutes
import io.github.carlawarde.kotlinBackendDemo.infrastructure.plugins.configureSerialization
import io.github.carlawarde.kotlinBackendDemo.infrastructure.plugins.configureStatusPages
import io.ktor.server.application.*
import io.ktor.server.netty.*
import mu.KotlinLogging

val logger = KotlinLogging.logger {}

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    logger.info("Loading configuration...")
    val appConfig = loadAppConfig(environment.config)

    logger.info("Configuring plugins...")
    val registry = configureMonitoring()
    val database = configureDatabase(appConfig.database, registry)
    val appInfoService = AppInfoService(database)
    configureDependencyInjection(database)
    configureSerialization()
    configureStatusPages()
    configureRoutes(appInfoService, registry)
    HealthMetrics.build(registry, appInfoService)

    appInfoService.setStatus(State.RUNNING)

    this.monitor.subscribe(ApplicationStopping) {
        logger.info("Applcation is shutting down...")
        appInfoService.setStatus(State.DRAINING)
    }

    this.monitor.subscribe(ApplicationStopped) {
        logger.info("Releasing application resources...")
        database.stop()
        appInfoService.setStatus(State.STOPPED)
    }
}