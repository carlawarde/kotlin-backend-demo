package io.github.carlawarde.kotlinBackendDemo

import io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle.JobRunner
import io.github.carlawarde.kotlinBackendDemo.infrastructure.config.AppConfig
import io.github.carlawarde.kotlinBackendDemo.infrastructure.modules.installAppDependencies
import io.github.carlawarde.kotlinBackendDemo.infrastructure.modules.configureDatabase
import io.github.carlawarde.kotlinBackendDemo.infrastructure.modules.configureMonitoring
import io.github.carlawarde.kotlinBackendDemo.infrastructure.modules.configureRouting
import io.github.carlawarde.kotlinBackendDemo.infrastructure.modules.configureSerialization
import io.github.carlawarde.kotlinBackendDemo.infrastructure.modules.configureStatusPages
import io.github.carlawarde.kotlinBackendDemo.infrastructure.monitoring.configureHealthGauges
import io.ktor.server.application.*
import io.ktor.server.netty.*
import org.slf4j.LoggerFactory

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    val logger = LoggerFactory.getLogger(Application::class.java)

    val appConfig = AppConfig(environment.config)
    logger.info("Configuring modules...")

    val metricsRegistry = configureMonitoring()
    val database = configureDatabase(appConfig, metricsRegistry)
    configureHealthGauges(metricsRegistry)
    configureSerialization()
    configureStatusPages()
    configureRouting(metricsRegistry, database)

    logger.info("Installing app dependencies...")
    val jobRunner = JobRunner(metricsRegistry)
    installAppDependencies(database, jobRunner)

    this.monitor.subscribe(ApplicationStopping) {
        logger.info("Shutting down application...")
        jobRunner.shutdown()
        database.stop()
    }
}