package io.github.carlawarde.kotlinBackendDemo.core.db

import org.jetbrains.exposed.v1.core.dao.id.UUIDTable
import org.jetbrains.exposed.v1.datetime.timestamp
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
object Users : UUIDTable("users") {
    val username = varchar("username", 30)
    val email = varchar("email", 255)
    val passwordHash = varchar("password_hash", 128)

    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)
}