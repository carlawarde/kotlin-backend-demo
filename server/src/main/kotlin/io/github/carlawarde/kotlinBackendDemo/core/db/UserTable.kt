package io.github.carlawarde.kotlinBackendDemo.core.db

import org.jetbrains.exposed.v1.core.dao.id.UUIDTable
import org.jetbrains.exposed.v1.datetime.timestamp
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
object Users : UUIDTable("users") {
    val username = varchar("username", 30).uniqueIndex()
    val email = varchar("email", 255).uniqueIndex()
    val passwordHash = varchar("password_hash", 128)

    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
}