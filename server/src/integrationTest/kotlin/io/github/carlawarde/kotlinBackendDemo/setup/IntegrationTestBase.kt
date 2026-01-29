package io.github.carlawarde.kotlinBackendDemo.setup

import io.kotest.core.extensions.install
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.JdbcDatabaseContainerProjectExtension

abstract class IntegrationTestBase : FunSpec({

    install(JdbcDatabaseContainerProjectExtension(PostgresTestContainer.container))

    beforeSpec {
        IntegrationTestConfig.initialize()
    }

    beforeTest {
        // Example: databaseManager.truncateTables("users", "orders")
    }

    afterTest {
        // Example: databaseManager.truncateTables("users", "orders")
    }

    afterSpec {
        IntegrationTestConfig.clear()
    }

})