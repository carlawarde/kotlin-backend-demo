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
    private lateinit var dataSource: HikariDataSource

    fun start() {
        dataSource = createDataSource()
        //runFlyway(datasource)
        Database.connect(dataSource)
    }

    fun stop() {
        if (::dataSource.isInitialized) {
            dataSource.close()
        }
    }

    fun isConnected(): Boolean {
        if (!::dataSource.isInitialized) {
            logger.warn("DataSource not initialized yet")
            return false
        }

        return try {
            dataSource.connection.use { conn ->
                conn.isValid(2)
            }
        } catch (e: Exception) {
            logger.error("Error database connection", e)
            false
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