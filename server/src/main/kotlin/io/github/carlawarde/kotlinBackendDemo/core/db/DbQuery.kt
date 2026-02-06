package io.github.carlawarde.kotlinBackendDemo.core.db

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.slf4j.MDCContext
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

suspend fun <T> dbQuery(db: Database, block: suspend () -> T): T =
    suspendTransaction(db) {
        withContext(Dispatchers.IO + MDCContext()) {
            block()
        }
    }
