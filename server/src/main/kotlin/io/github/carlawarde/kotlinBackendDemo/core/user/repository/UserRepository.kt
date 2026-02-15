package io.github.carlawarde.kotlinBackendDemo.core.user.repository

import io.github.carlawarde.kotlinBackendDemo.common.errors.AppException
import io.github.carlawarde.kotlinBackendDemo.core.db.Users
import io.github.carlawarde.kotlinBackendDemo.core.db.Users.createdAt
import io.github.carlawarde.kotlinBackendDemo.core.db.Users.email
import io.github.carlawarde.kotlinBackendDemo.core.db.Users.id
import io.github.carlawarde.kotlinBackendDemo.core.db.Users.passwordHash
import io.github.carlawarde.kotlinBackendDemo.core.db.Users.updatedAt
import io.github.carlawarde.kotlinBackendDemo.core.db.Users.username
import io.github.carlawarde.kotlinBackendDemo.core.db.dbQuery
import io.github.carlawarde.kotlinBackendDemo.core.errors.UserDomainError
import io.github.carlawarde.kotlinBackendDemo.core.user.domain.User
import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.types.DatabaseProvider
import mu.KotlinLogging
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.exceptions.ExposedSQLException
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.postgresql.util.PSQLException
import java.util.UUID
import kotlin.text.lowercase

interface UserRepository {
    suspend fun create(username: String, email: String, passwordHash: String): User
    suspend fun findByEmail(email: String): User?
    suspend fun findById(id: UUID): User?
}

class UserRepositoryImpl(private val dbProvider: DatabaseProvider) : UserRepository {
    val logger = KotlinLogging.logger {}

    override suspend fun create(
        username: String,
        email: String,
        passwordHash: String
    ): User {
        logger.info { "Inserting new user into database" }

        return dbQuery(dbProvider.db) {
            try {
                val userUuid = UUID.randomUUID()
                Users.insert { statement ->
                    statement[this.id] = userUuid
                    statement[this.username] = username
                    statement[this.email] = email
                    statement[this.passwordHash] = passwordHash
                }

                Users
                    .selectAll()
                    .where { id eq userUuid }
                    .single()
                    .let(::toUser)

            } catch (e: ExposedSQLException) {
                // PostgreSQL unique constraint violation code = 23505
                if (e.cause is PSQLException && (e.cause as PSQLException).sqlState == "23505") {
                    val constraintName = (e.cause as PSQLException).serverErrorMessage?.constraint
                    when (constraintName) {
                        "uidx_users_username" -> {
                            val error = UserDomainError.UsernameAlreadyTaken
                            logger.warn { error.logMessage }
                            throw AppException(error)
                        }
                        "uidx_users_email" -> {
                            val error = UserDomainError.EmailAlreadyTaken
                            logger.warn { error.logMessage }
                            throw AppException(error)
                        }
                        else -> throw e
                    }
                } else {
                    throw e
                }
            }
        }
    }

    override suspend fun findByEmail(email: String): User? {
        logger.info { "Getting user by email" }

        return dbQuery(dbProvider.db) {
            Users
                .selectAll()
                .where { Users.email eq email.lowercase() }
                .singleOrNull()
                ?.let(::toUser)
        }
    }

    override suspend fun findById(id: UUID): User? {
        logger.info { "Getting user by id: $id" }

        return dbQuery(dbProvider.db) {
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
            createdAt = row[createdAt].toInstant(),
            updatedAt = row[updatedAt].toInstant()
        )
    }
}