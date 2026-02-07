@file:OptIn(ExperimentalTime::class)

package io.github.carlawarde.kotlinBackendDemo.spec

import io.github.carlawarde.kotlinBackendDemo.core.db.Users
import io.github.carlawarde.kotlinBackendDemo.core.user.domain.User
import io.github.carlawarde.kotlinBackendDemo.core.user.domain.UserFactory
import io.github.carlawarde.kotlinBackendDemo.core.user.dto.CreateUserRequest
import io.github.carlawarde.kotlinBackendDemo.core.user.repository.UserRepository
import io.github.carlawarde.kotlinBackendDemo.core.user.repository.UserRepositoryImpl
import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.DatabaseManager
import io.github.carlawarde.kotlinBackendDemo.setup.IntegrationTestBase
import io.github.carlawarde.kotlinBackendDemo.setup.IntegrationTestBaseConfig
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.deleteAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.util.UUID
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class UserRepositoryIntegrationTest : IntegrationTestBase() {
    init {
        lateinit var databaseManager: DatabaseManager
        lateinit var repository: UserRepository

        beforeSpec {
            val config = IntegrationTestBaseConfig.getAsDatabaseConfig()
            val registry = SimpleMeterRegistry()

            databaseManager = DatabaseManager(config,registry)
            databaseManager.start()

            transaction(databaseManager.db) {
                SchemaUtils.create(Users)
            }

            repository = UserRepositoryImpl(databaseManager.db)
        }

        afterSpec {
            databaseManager.stop()
        }

        beforeTest {
            transaction(databaseManager.db) {
                Users.deleteAll()
            }
        }

        fun generateUser(userName: String, email: String): User {
            val now = Clock.System.now()
            return User(
                UUID.randomUUID(),
                userName,
                "test_pwd",
                email,
                now,
                now
            )
        }

        test("create should create a new user successfully") {
            val user = generateUser("John321", "john321@test.com")

            val created = repository.create(user)

            created.id shouldBe user.id
            created.username shouldBe user.username
            created.email shouldBe user.email
        }

        test("find existing user by email should return user") {
            val email = "jane@example.com"
            val userName = "jane_doe"
            val user = generateUser(userName, email)
            repository.create(user)

            val found = repository.findByEmail(email)
            found.shouldNotBeNull()
            found.username shouldBe user.username
        }

        test("find non-existing user by email should return null") {
            val found = repository.findByEmail("nonexistent@example.com")
            found.shouldBeNull()
        }
    }

}