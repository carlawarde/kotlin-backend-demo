package io.github.carlawarde.kotlinBackendDemo.spec

import io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle.AppInfoService
import io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle.AppState
import io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle.appInfoRoutes
import io.github.carlawarde.kotlinBackendDemo.setup.IntegrationTestBase
import io.github.carlawarde.kotlinBackendDemo.setup.buildTestApp
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.matchers.string.shouldContain
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication

class AppInfoSpec : IntegrationTestBase() {

    init {
        test("liveness should be OK when db is up") {
            testApplication {
                val client = buildTestApp(startDatabase = true) { deps ->
                    routing {
                        appInfoRoutes(AppInfoService(deps.databaseService))
                    }
                }

                val response = client.get("/health/live")
                response shouldHaveStatus HttpStatusCode.OK
            }
        }

        test("readiness should be OK when db is up") {
            testApplication {
                val client = buildTestApp(startDatabase = true) { deps ->
                    routing {
                        appInfoRoutes(AppInfoService(deps.databaseService))
                    }
                }

                val response = client.get("/health/ready")
                response shouldHaveStatus HttpStatusCode.OK
            }
        }

        test("status should be RUNNING when application is up") {
            testApplication {
                val client = buildTestApp(startDatabase = true) { deps ->
                    val appInfoService = AppInfoService(deps.databaseService)
                    appInfoService.setStatus(AppState.RUNNING)
                    routing {
                        appInfoRoutes(appInfoService)
                    }
                }

                val response = client.get("/health/status")
                response.bodyAsText() shouldContain "RUNNING"
            }
        }
    }
}