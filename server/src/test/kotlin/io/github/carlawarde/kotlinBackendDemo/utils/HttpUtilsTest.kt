package io.github.carlawarde.kotlinBackendDemo.utils

import io.github.carlawarde.kotlinBackendDemo.core.errors.FieldValidationError
import io.github.carlawarde.kotlinBackendDemo.core.errors.ForbiddenError
import io.github.carlawarde.kotlinBackendDemo.core.errors.UnauthorizedError
import io.github.carlawarde.kotlinBackendDemo.core.errors.ValidationError
import io.github.carlawarde.kotlinBackendDemo.utils.HttpUtils.toHttpStatus
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode

class HttpUtilsTest: FunSpec( {

    test("Unauthorized maps to 401") {
        UnauthorizedError.toHttpStatus() shouldBe HttpStatusCode.Unauthorized
    }

    test("Forbidden maps to 403") {
        ForbiddenError.toHttpStatus() shouldBe HttpStatusCode.Forbidden
    }

    test("ValidationError maps to 401") {
        val validationError = ValidationError("", listOf(FieldValidationError("field", "reason")))
        validationError.toHttpStatus() shouldBe HttpStatusCode.BadRequest
    }
})