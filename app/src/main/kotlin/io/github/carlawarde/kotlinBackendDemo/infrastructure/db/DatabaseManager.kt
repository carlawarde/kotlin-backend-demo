package io.github.carlawarde.kotlinBackendDemo.infrastructure.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.carlawarde.kotlinBackendDemo.infrastructure.config.AppConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.v1.jdbc.Database

class DatabaseManager(private val config: AppConfig, private val meterRegistry: PrometheusMeterRegistry) {
    private lateinit var dataSource: HikariDataSource

    fun start() {
        val datasource = createDataSource()
        runFlyway(datasource)
        Database.connect(datasource)
    }

    fun stop() {
        if (::dataSource.isInitialized) {
            dataSource.close()
        }
    }

    private fun createDataSource(): HikariDataSource {
        val hikariConfig = HikariConfig().apply {
            jdbcUrl = config.dbUrl
            username = config.dbUser
            password = config.dbPassword
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            this.metricRegistry = meterRegistry
            validate()
        }
        return HikariDataSource(hikariConfig)
    }

    private fun runFlyway(dataSource: HikariDataSource) {
        val flyway = Flyway.configure().dataSource(dataSource).load()
        flyway.migrate()
    }
}