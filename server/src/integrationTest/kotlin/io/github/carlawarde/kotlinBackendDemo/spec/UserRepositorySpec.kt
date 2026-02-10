package io.github.carlawarde.kotlinBackendDemo.spec

import io.github.carlawarde.kotlinBackendDemo.core.db.Users
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
            val email = "joe@example.com"
            val username = "joe123"
            val password = "password"
            val created = repository.create(username, email, password)

            created.username shouldBe username
            created.email shouldBe email
        }

        test("find existing user by email should return user") {
            val email = "jane@example.com"
            val username = "jane_doe"

            repository.create(username, email, "pwd-321")

            val found = repository.findByEmail(email)
            found.shouldNotBeNull()
            found.username shouldBe username
        }

        test("find non-existing user by email should return null") {
            val found = repository.findByEmail("nonexistent@example.com")
            found.shouldBeNull()
        }
    }

}