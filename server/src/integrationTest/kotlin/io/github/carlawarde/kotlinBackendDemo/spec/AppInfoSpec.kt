package io.github.carlawarde.kotlinBackendDemo.spec

import io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle.AppInfoService
import io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle.State
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
        test("liveness is OK when db is up") {
            testApplication {
                val client = buildTestApp { deps ->
                    routing {
                        appInfoRoutes(AppInfoService(deps.databaseManager))
                    }
                }

                val response = client.get("/health/live")
                response shouldHaveStatus HttpStatusCode.OK
            }
        }

        test("readiness is OK when db is up") {
            testApplication {
                val client = buildTestApp { deps ->
                    routing {
                        appInfoRoutes(AppInfoService(deps.databaseManager))
                    }
                }

                val response = client.get("/health/ready")
                response shouldHaveStatus HttpStatusCode.OK
            }
        }

        test("status is RUNNING when application is up") {
            testApplication {
                val client = buildTestApp { deps ->
                    val appInfoService = AppInfoService(deps.databaseManager)
                    appInfoService.setStatus(State.RUNNING)
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