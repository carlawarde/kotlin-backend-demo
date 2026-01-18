package io.github.carlawarde.kotlinBackendDemo

import io.github.carlawarde.kotlinBackendDemo.infrastructure.ApplicationCoroutineScope
import io.github.carlawarde.kotlinBackendDemo.infrastructure.config.AppConfig
import io.github.carlawarde.kotlinBackendDemo.infrastructure.modules.installAppDependencies
import io.github.carlawarde.kotlinBackendDemo.infrastructure.modules.configureDatabase
import io.github.carlawarde.kotlinBackendDemo.infrastructure.modules.configureMonitoring
import io.github.carlawarde.kotlinBackendDemo.infrastructure.modules.configureRouting
import io.github.carlawarde.kotlinBackendDemo.infrastructure.modules.configureSerialization
import io.github.carlawarde.kotlinBackendDemo.infrastructure.modules.configureStatusPages
import io.ktor.server.application.*
import io.ktor.server.netty.*
import org.slf4j.LoggerFactory

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    val logger = LoggerFactory.getLogger("Application")

    val appConfig = AppConfig(environment.config)
    logger.info("Configuring modules...")

    val metrics = configureMonitoring()
    val database = configureDatabase(appConfig, metrics)
    configureSerialization()
    configureStatusPages()
    configureRouting()

    logger.info("Installing app dependencies...")
    val appScope = ApplicationCoroutineScope()
    installAppDependencies(database, appScope)

    this.monitor.subscribe(ApplicationStopping) {
        logger.info("Shutting down application...")
        appScope.shutdown()
        database.stop()
    }
}