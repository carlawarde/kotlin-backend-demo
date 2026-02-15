package io.github.carlawarde.kotlinBackendDemo.spec

import io.github.carlawarde.kotlinBackendDemo.setup.IntegrationTestBase
import io.github.carlawarde.kotlinBackendDemo.setup.buildTestApp
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.matchers.shouldBe
import io.ktor.server.testing.testApplication

class DatabaseManagerIntegrationSpec : IntegrationTestBase() {

    init {

        test("start initializes datasource and isConnected returns true") {
            testApplication {
                buildTestApp {  deps ->
                    val dbManager = deps.databaseService
                    dbManager.isConnected() shouldBe false
                    dbManager.start()
                    dbManager.isConnected() shouldBe true
                }
            }
        }

        test("start is idempotent") {
            testApplication {
                buildTestApp { deps ->
                    val dbManager = deps.databaseService
                    dbManager.start()
                    dbManager.start()

                    dbManager.isConnected() shouldBe true
                }
            }
        }

        test("stop closes the datasource safely") {
            testApplication {
                buildTestApp { deps ->
                    val dbManager = deps.databaseService

                    dbManager.start()
                    dbManager.stop()

                    dbManager.isConnected() shouldBe false

                    shouldNotThrowAny { dbManager.stop() }
                }
            }
        }

        test("start after stop is allowed") {
            testApplication {
                buildTestApp { deps ->
                    val dbManager = deps.databaseService

                    dbManager.start()
                    dbManager.stop()
                    dbManager.start()

                    dbManager.isConnected() shouldBe true
                }
            }
        }
    }
}
