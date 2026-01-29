package io.github.carlawarde.kotlinBackendDemo.infrastructure.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.carlawarde.kotlinBackendDemo.infrastructure.config.DatabaseConfig
import io.micrometer.core.instrument.MeterRegistry
import mu.KotlinLogging
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.v1.jdbc.Database

class DatabaseManager(private val config: DatabaseConfig, private val meterRegistry: MeterRegistry) {
    private val logger = KotlinLogging.logger {}
    private var dataSource: HikariDataSource? = null

    fun start() {
        if (dataSource != null) {
            logger.info("DataSource already started, skipping initialization...")
        } else {
            dataSource = createDataSource()
            //runFlyway(datasource)
            Database.connect(dataSource!!)
        }
    }

    fun stop() {
        if (dataSource == null) {
            logger.warn("DataSource is not initialized, skipping stop...")
        } else {
            dataSource?.close()
            dataSource = null
        }
    }

    fun isConnected(): Boolean {
        if (dataSource == null) {
            logger.warn("DataSource is not initialized, skipping connection check...")
            return false
        } else {
            return try {
                dataSource!!.connection.use { conn ->
                    conn.isValid(2)
                }
            } catch (e: Exception) {
                logger.error("Error database connection", e)
                false
            }
        }
    }

    private fun createDataSource(): HikariDataSource {
        logger.info("Creating HikariDataSource...")

        val hikariConfig = HikariConfig().apply {
            jdbcUrl = config.jdbcUrl
            username = config.user
            password = config.password
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            this.metricRegistry = meterRegistry
            validate()
        }
        return HikariDataSource(hikariConfig)
    }

    private fun runFlyway(dataSource: HikariDataSource) {
        logger.info("Starting Flyway migration...")
        val flyway = Flyway.configure().dataSource(dataSource).load()
        flyway.migrate()
    }
}