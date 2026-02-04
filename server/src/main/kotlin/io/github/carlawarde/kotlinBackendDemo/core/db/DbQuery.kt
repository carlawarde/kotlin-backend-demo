package io.github.carlawarde.kotlinBackendDemo.core.db

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.slf4j.MDCContext
import kotlinx.coroutines.withContext
import mu.KLogger
import mu.KotlinLogging
import org.jetbrains.exposed.v1.exceptions.ExposedSQLException
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

suspend fun <T> dbQuery(
    db: Database,
    logger : KLogger = KotlinLogging.logger {},
    block: suspend () -> T
): T =
    suspendTransaction(db) {
        withContext(Dispatchers.IO) { MDCContext() }
        try {
            block()
        } catch (e: ExposedSQLException) {
            logger.error(e) { "Error during transaction: ${e.message}" }
            throw e
        }
    }
