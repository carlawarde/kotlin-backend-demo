package io.github.carlawarde.kotlinBackendDemo.core.user.repository

import io.github.carlawarde.kotlinBackendDemo.core.db.Users
import io.github.carlawarde.kotlinBackendDemo.core.db.Users.createdAt
import io.github.carlawarde.kotlinBackendDemo.core.db.Users.email
import io.github.carlawarde.kotlinBackendDemo.core.db.Users.id
import io.github.carlawarde.kotlinBackendDemo.core.db.Users.passwordHash
import io.github.carlawarde.kotlinBackendDemo.core.db.Users.updatedAt
import io.github.carlawarde.kotlinBackendDemo.core.db.Users.username
import io.github.carlawarde.kotlinBackendDemo.core.db.dbQuery
import io.github.carlawarde.kotlinBackendDemo.core.user.domain.User
import mu.KotlinLogging
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import java.util.UUID
import kotlin.text.lowercase
import kotlin.time.ExperimentalTime

interface UserRepository {
    suspend fun create(user: User): User
    suspend fun findByEmail(email: String): User?
    suspend fun findById(id: UUID): User?
}

@OptIn(ExperimentalTime::class)
class UserRepositoryImpl(val db: Database) : UserRepository {
    val logger = KotlinLogging.logger {}

    override suspend fun create(user: User): User {
        logger.info { "Inserting new user into database" }

        return dbQuery(db, logger) {
            val row = Users.insert {
                    it[id] = user.id
                    it[username] = user.username
                    it[email] = user.email
                    it[passwordHash] = user.passwordHash
                }.resultedValues!!.first()

            toUser(row)
        }
    }

    override suspend fun findByEmail(email: String): User? {
        logger.info { "Getting user by email" }

        return dbQuery(db, logger) {
            Users
                .selectAll()
                .where { Users.email eq email.lowercase() }
                .singleOrNull()
                ?.let(::toUser)
        }
    }

    override suspend fun findById(id: UUID): User? {
        logger.info { "Getting user by id: $id" }

        return dbQuery(db, logger) {
            Users
                .selectAll()
                .where(Users.id eq(id))
                .singleOrNull()
                ?.let(::toUser)
        }
    }

    private fun toUser(row: ResultRow): User {
        return User(
            id = row[id].value,
            username = row[username],
            email = row[email],
            passwordHash = row[passwordHash],
            createdAt = row[createdAt],
            updatedAt = row[updatedAt]
        )
    }
}