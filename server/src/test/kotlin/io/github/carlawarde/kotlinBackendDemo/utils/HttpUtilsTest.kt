package io.github.carlawarde.kotlinBackendDemo.utils

import io.github.carlawarde.kotlinBackendDemo.core.errors.AuthError
import io.github.carlawarde.kotlinBackendDemo.core.errors.RequestFieldValidationError
import io.github.carlawarde.kotlinBackendDemo.core.errors.RequestValidationError
import io.github.carlawarde.kotlinBackendDemo.utils.HttpUtils.toHttpStatus
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode

class HttpUtilsTest: FunSpec( {

    test("Unauthorized maps to 401") {
        AuthError.UnauthorizedError.toHttpStatus() shouldBe  HttpStatusCode.Unauthorized
    }

    test("Forbidden maps to 403") {
        AuthError.ForbiddenError.toHttpStatus() shouldBe HttpStatusCode.Forbidden
    }

    test("ValidationError maps to 401") {
        val requestValidationError = RequestValidationError("", listOf(RequestFieldValidationError("field", "reason")))
        requestValidationError.toHttpStatus() shouldBe HttpStatusCode.BadRequest
    }
})