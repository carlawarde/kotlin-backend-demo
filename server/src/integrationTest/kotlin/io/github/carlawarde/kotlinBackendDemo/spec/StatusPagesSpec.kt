package io.github.carlawarde.kotlinBackendDemo.spec

import io.github.carlawarde.kotlinBackendDemo.common.errors.AppException
import io.github.carlawarde.kotlinBackendDemo.core.errors.FieldValidationError
import io.github.carlawarde.kotlinBackendDemo.core.errors.RequestValidationError
import io.github.carlawarde.kotlinBackendDemo.core.user.dto.PostRegisterUserRequest
import io.github.carlawarde.kotlinBackendDemo.http.dto.ErrorResponse
import io.github.carlawarde.kotlinBackendDemo.http.dto.ValidationErrorResponse
import io.github.carlawarde.kotlinBackendDemo.http.errors.AuthError
import io.github.carlawarde.kotlinBackendDemo.infrastructure.errors.SystemError
import io.github.carlawarde.kotlinBackendDemo.setup.IntegrationTestBase
import io.github.carlawarde.kotlinBackendDemo.setup.buildTestApp
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication

class StatusPagesSpec : IntegrationTestBase() {

    init {

        test("ErrorResponse should return correct status and error body for AppException") {
            testApplication {
                val client = buildTestApp { _ ->

                    this.routing {
                        get("/test/app-error") {
                            throw AppException(AuthError.Unauthorized)
                        }
                    }
                }

                val response = client.get("/test/app-error")
                response.status shouldBe AuthError.Unauthorized.statusCode

                val body: ErrorResponse = response.body()
                body.message shouldBe AuthError.Unauthorized.userMessage
                body.internalCode shouldBe AuthError.Unauthorized.internalCode
            }
        }

        test("ValidationErrorResponse should return correct status and error body for AppException") {
            testApplication {
                val client = buildTestApp { _ ->

                    this.routing {
                        get("/test/app-error") {
                            val listFieldValidation = listOf(FieldValidationError("username", listOf("test")))
                            val validationError = RequestValidationError("Test Error", listFieldValidation)
                            throw AppException(validationError)
                        }
                    }
                }

                val response = client.get("/test/app-error")
                response.status shouldBe HttpStatusCode.BadRequest
                val body = response.body<ValidationErrorResponse>()
                body.message shouldBe "Test Error"
                body.internalCode shouldBe "VAL_001"
                body.fields["username"] shouldBe listOf("test")
            }
        }

        test("unknown route should return 404 response") {
            testApplication {
                val client = buildTestApp {}

                val response = client.get("/does-not-exist")
                response.status shouldBe HttpStatusCode.NotFound

                val body: ErrorResponse = response.body() as ErrorResponse
                body.message shouldBe SystemError.NotFoundError.userMessage
                body.internalCode shouldBe SystemError.NotFoundError.internalCode
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
                body.message shouldBe SystemError.InternalServerError.userMessage
                body.internalCode shouldBe SystemError.InternalServerError.internalCode
            }
        }

        test("missing required field returns 400") {
            testApplication {
                val client = buildTestApp { _ ->
                    routing {
                        post("/register") {
                            call.receive<PostRegisterUserRequest>()
                            call.respond(HttpStatusCode.OK)
                        }
                    }
                }

                val response = client.post("/register") {
                    contentType(ContentType.Application.Json)
                    setBody("""
                {
                    "username": "bob",
                    "email": "bob@test.com"
                }
            """.trimIndent())
                }

                response.status shouldBe HttpStatusCode.BadRequest
            }
        }

        test("malformed json returns 400") {
            testApplication {
                val client = buildTestApp { _ ->
                    routing {
                        post("/register") {
                            call.receive<PostRegisterUserRequest>()
                            call.respond(HttpStatusCode.OK)
                        }
                    }
                }

                val response = client.post("/register") {
                    contentType(ContentType.Application.Json)
                    setBody("{ not valid json }")
                }

                response.status shouldBe HttpStatusCode.BadRequest
            }
        }


    }
}
