package io.github.carlawarde.kotlinBackendDemo.spec

import io.github.carlawarde.kotlinBackendDemo.core.errors.AppException
import io.github.carlawarde.kotlinBackendDemo.core.errors.Unauthorized
import io.github.carlawarde.kotlinBackendDemo.infrastructure.errors.InternalServerError
import io.github.carlawarde.kotlinBackendDemo.infrastructure.errors.NotFoundError
import io.github.carlawarde.kotlinBackendDemo.infrastructure.http.ErrorResponse
import io.github.carlawarde.kotlinBackendDemo.setup.IntegrationTestBase
import io.github.carlawarde.kotlinBackendDemo.setup.buildTestApp
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication

class StatusPagesSpec : IntegrationTestBase() {

    init {

        test("ErrorResponse should return correct status and error body for AppException") {
            testApplication {
                val client = buildTestApp { _ ->

                    this.routing {
                        get("/test/app-error") {
                            throw AppException(Unauthorized)
                        }
                    }
                }

                val response = client.get("/test/app-error")
                response.status shouldBe HttpStatusCode.Unauthorized

                val body: ErrorResponse = response.body()
                body.message shouldBe Unauthorized.message
                body.code shouldBe Unauthorized.code
            }
        }

        test("unknown route should return 404 response") {
            testApplication {
                val client = buildTestApp {}

                val response = client.get("/does-not-exist")
                response.status shouldBe HttpStatusCode.NotFound

                val body: ErrorResponse = response.body() as ErrorResponse
                body.message shouldBe NotFoundError.message
                body.code shouldBe NotFoundError.code
            }
        }

        test("unexpected exception should return 500 response without leaking internal details") {
            testApplication {
                val client = buildTestApp { _ ->
                    routing {
                        get("/test/crash") {
                            error("boom")
                        }
                    }
                }

                val response = client.get("/test/crash")
                response.status shouldBe HttpStatusCode.InternalServerError

                val body: ErrorResponse = response.body()
                body.message shouldBe InternalServerError.message
                body.code shouldBe InternalServerError.code
            }
        }
    }
}
