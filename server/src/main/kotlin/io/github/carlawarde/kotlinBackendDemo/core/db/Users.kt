package io.github.carlawarde.kotlinBackendDemo.core.db

import org.jetbrains.exposed.v1.core.dao.id.UUIDTable
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone

object Users : UUIDTable("users") {
    val username = citext("username")
    val email = citext("email")
    val passwordHash = varchar("password_hash", 128)

    val createdAt = timestampWithTimeZone("created_at")
    val updatedAt = timestampWithTimeZone("updated_at")
}