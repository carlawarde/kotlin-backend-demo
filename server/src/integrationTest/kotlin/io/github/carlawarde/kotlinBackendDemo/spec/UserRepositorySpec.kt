@file:OptIn(ExperimentalTime::class)

package io.github.carlawarde.kotlinBackendDemo.spec

import io.github.carlawarde.kotlinBackendDemo.core.db.Users
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

        test("create should create a new user successfully") {
            val user = UserFactory.fromDTO(
                dto = CreateUserRequest("john_doe", "john@example.com", "password123"),
                hashPassword = { it }
            )

            val created = repository.create(user)

            created.id shouldBe user.id
            created.username shouldBe "john_doe"
            created.email shouldBe "john@example.com"
        }

        test("find existing user by email should return user") {
            val user = UserFactory.fromDTO(
                dto = CreateUserRequest("jane_doe", "jane@example.com", "secret"),
                hashPassword = { it }
            )
            repository.create(user)

            val found = repository.findByEmail("jane@example.com")
            found.shouldNotBeNull()
            found.username shouldBe "jane_doe"
        }

        test("find non-existing user by email should return null") {
            val found = repository.findByEmail("nonexistent@example.com")
            found.shouldBeNull()
        }
    }

}