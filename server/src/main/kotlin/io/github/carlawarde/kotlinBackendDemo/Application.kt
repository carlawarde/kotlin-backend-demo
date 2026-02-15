package io.github.carlawarde.kotlinBackendDemo


import ch.qos.logback.classic.LoggerContext
import io.github.carlawarde.kotlinBackendDemo.infrastructure.observability.metrics.ApiMetrics
import io.github.carlawarde.kotlinBackendDemo.infrastructure.config.loadAppConfig
import io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle.AppInfoService
import io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle.AppState
import io.github.carlawarde.kotlinBackendDemo.infrastructure.observability.metrics.HealthMetrics
import io.github.carlawarde.kotlinBackendDemo.infrastructure.plugins.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import mu.KotlinLogging
import org.slf4j.LoggerFactory

val logger = KotlinLogging.logger {}

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    logger.info("Loading configuration...")
    val appConfig = loadAppConfig(environment.config)

    logger.info("Configuring plugins...")
    val registry = configureMonitoring(appConfig.metrics)
    val databaseManager = DatabaseSetup.configure(appConfig.database, registry)
    configureDependencyInjection(databaseManager)
    configureSerialization()
    configureStatusPages()
    configureRateLimit()

    val appInfoService = AppInfoService(databaseManager)
    HealthMetrics.build(registry, appInfoService)
    ApiMetrics.build(registry)

    configureRoutes(appInfoService, registry, databaseManager)

    appInfoService.setStatus(AppState.RUNNING)

    this.monitor.subscribe(ApplicationStopping) {
        logger.info("Application is shutting down...")
        appInfoService.setStatus(AppState.DRAINING)
    }

    this.monitor.subscribe(ApplicationStopped) {
        logger.info("Releasing application resources...")
        databaseManager.stop()
        val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
        loggerContext.stop()
        appInfoService.setStatus(AppState.STOPPED)
    }
}