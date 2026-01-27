package io.github.carlawarde.kotlinBackendDemo


import ch.qos.logback.classic.LoggerContext
import io.github.carlawarde.kotlinBackendDemo.infrastructure.config.loadAppConfig
import io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle.AppInfoService
import io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle.State
import io.github.carlawarde.kotlinBackendDemo.infrastructure.monitoring.HealthMetrics
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
    val database = configureDatabase(appConfig.database, registry)
    configureDependencyInjection(database)
    configureSerialization()
    configureStatusPages()

    val appInfoService = AppInfoService(database)
    configureRoutes(appInfoService, registry)

    HealthMetrics.build(registry, appInfoService)
    //AppJobMetrics.build(registry)

    appInfoService.setStatus(State.RUNNING)

    this.monitor.subscribe(ApplicationStopping) {
        logger.info("Application is shutting down...")
        appInfoService.setStatus(State.DRAINING)
    }

    this.monitor.subscribe(ApplicationStopped) {
        logger.info("Releasing application resources...")
        database.stop()
        val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
        loggerContext.stop()
        appInfoService.setStatus(State.STOPPED)
    }
}