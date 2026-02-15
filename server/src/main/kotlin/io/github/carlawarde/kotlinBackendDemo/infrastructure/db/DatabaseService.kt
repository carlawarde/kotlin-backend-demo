package io.github.carlawarde.kotlinBackendDemo.infrastructure.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.carlawarde.kotlinBackendDemo.infrastructure.config.DatabaseConfig
import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.types.DatabaseProvider
import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.types.DatabaseState
import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.types.DatabaseStatus
import io.micrometer.core.instrument.MeterRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.FlywayException
import org.jetbrains.exposed.v1.jdbc.Database
import java.util.concurrent.atomic.AtomicReference

class DatabaseService(
    private val config: DatabaseConfig,
    private val meterRegistry: MeterRegistry,
    private val reconnectDelayMillis: Long = 20000L, // base reconnect delay
    private val reconnectJitterMillis: Long = 2000L, // random jitter
) : DatabaseProvider, DatabaseStatus {

    private val logger = KotlinLogging.logger {}

    private val state = AtomicReference(DatabaseState.STARTING)
    private val dataSourceRef = AtomicReference<HikariDataSource?>(null)
    private val databaseRef = AtomicReference<Database?>(null)

    private val reconnectJob = AtomicReference<Job?>(null)
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override val db: Database
        get() = databaseRef.get() ?: error("Database not available. Current state: ${state.get()}")

    override fun isReady(): Boolean = state.get() == DatabaseState.CONNECTED

    override fun isConnected(): Boolean {
        if (state.get() != DatabaseState.CONNECTED) return false

        return try {
            dataSourceRef.get()?.connection?.use { it.isValid(2) } ?: false
        } catch (ex: Exception) {
            logger.warn(ex) { "Database connection validation failed..." }
            setState(DatabaseState.UNAVAILABLE)
            false
        }
    }

    fun start() {
        val current = state.get()

        if (current == DatabaseState.CONNECTED) {
            logger.info("Database connection already started...")
            return
        }

        attemptConnect()
        startReconnectMonitor()
    }

    fun stop() {
        logger.info("Stopping DatabaseService...")
        state.set(DatabaseState.STOPPED)
        reconnectJob.getAndSet(null)?.cancel()
        cleanup()
        scope.cancel()
    }

    private fun attemptConnect(): Boolean {
        return try {
            logger.info("Attempting database connection...")
            setState(DatabaseState.STARTING)

            val dataSource = createDataSource()
            runFlyway(dataSource)
            val database = Database.connect(dataSource)

            dataSourceRef.set(dataSource)
            databaseRef.set(database)

            logger.info("Database connected successfully...")
            setState(DatabaseState.CONNECTED)
            true

        } catch (ex: Exception) {
            logger.error("Database connection failed...", ex)
            cleanup()
            setState(DatabaseState.UNAVAILABLE)
            false
        }
    }

    private fun createDataSource(): HikariDataSource {
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
        try {
            Flyway.configure()
                .dataSource(dataSource)
                .load()
                .migrate()
            logger.info("Flyway migration completed successfully.")
        } catch (ex: FlywayException) {
            logger.error("Flyway migration failed", ex)
        }
    }

    private fun cleanup() {
        dataSourceRef.getAndSet(null)?.close()
        databaseRef.set(null)
    }

    private fun startReconnectMonitor() {
        reconnectJob.set(scope.launch {
            while (state.get() != DatabaseState.STOPPED) {
                if (!isConnected() && state.get() != DatabaseState.STARTING) {
                    logger.info("Database unavailable, current state is ${state.get()}...")
                    val jitter = (0..reconnectJitterMillis).random()
                    val delayMillis = reconnectDelayMillis + jitter

                    val connected = attemptConnect()
                    if (!connected) {
                        logger.info("Retrying database connection in $delayMillis ms...")
                    }
                    delay(delayMillis)
                } else {
                    delay(20000L)
                }
            }
        })
    }

    private fun setState(newState: DatabaseState) {
        when (newState) {
            DatabaseState.STARTING -> {
                val currentState = state.get()
                if (currentState == DatabaseState.STOPPED || currentState == DatabaseState.UNAVAILABLE) {
                    state.set(newState)
                }
            }
            DatabaseState.CONNECTED -> state.compareAndSet(DatabaseState.STARTING, newState)
            DatabaseState.UNAVAILABLE -> {
                val currentState = state.get()
                if (currentState == DatabaseState.STARTING || currentState == DatabaseState.CONNECTED) {
                    state.set(newState)
                }
            }
            DatabaseState.STOPPED -> state.set(DatabaseState.STOPPED)
        }
    }
}