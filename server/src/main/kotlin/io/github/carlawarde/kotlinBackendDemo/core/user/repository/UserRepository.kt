package io.github.carlawarde.kotlinBackendDemo.core.user.repository

import io.github.carlawarde.kotlinBackendDemo.common.errors.AppException
import io.github.carlawarde.kotlinBackendDemo.core.db.Users
import io.github.carlawarde.kotlinBackendDemo.core.db.Users.email
import io.github.carlawarde.kotlinBackendDemo.core.db.Users.id
import io.github.carlawarde.kotlinBackendDemo.core.db.Users.passwordHash
import io.github.carlawarde.kotlinBackendDemo.core.db.Users.username
import io.github.carlawarde.kotlinBackendDemo.core.db.dbQuery
import io.github.carlawarde.kotlinBackendDemo.core.errors.UserDomainError
import io.github.carlawarde.kotlinBackendDemo.core.user.domain.User
import mu.KotlinLogging
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.exceptions.ExposedSQLException
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.postgresql.util.PSQLException
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

        return dbQuery(db) {
            try {
                Users.insert {
                    it[id] = user.id
                    it[username] = user.username
                    it[email] = user.email
                    it[passwordHash] = user.passwordHash
                }

                Users
                    .selectAll()
                    .where { id eq user.id }
                    .single()
                    .let(::toUser)

            } catch (e: ExposedSQLException) {
                // PostgreSQL unique constraint violation code = 23505
                if (e.cause is PSQLException && (e.cause as PSQLException).sqlState == "23505") {
                    val constraintName = (e.cause as PSQLException).serverErrorMessage?.constraint
                    when (constraintName) {
                        "uidx_users_username" -> {
                            val error = UserDomainError.UsernameAlreadyTakenError
                            logger.warn { error.logMessage }
                            throw AppException(error)
                        }
                        "uidx_users_email" -> {
                            val error = UserDomainError.EmailAlreadyTakenError
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

        return dbQuery(db) {
            Users
                .selectAll()
                .where { Users.email eq email.lowercase() }
                .singleOrNull()
                ?.let(::toUser)
        }
    }

    override suspend fun findById(id: UUID): User? {
        logger.info { "Getting user by id: $id" }

        return dbQuery(db) {
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
            passwordHash = row[passwordHash]
        )
    }
}