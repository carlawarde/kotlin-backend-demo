package io.github.carlawarde.kotlinBackendDemo.core.user.validation

import io.github.carlawarde.kotlinBackendDemo.core.user.dto.PostRegisterUserRequest
import io.konform.validation.messagesAtPath
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class PostRegisterUserRequestValidationTest: FunSpec({

    fun validRequest() = PostRegisterUserRequest(
        username = "valid_user",
        email = "test@example.com",
        password = "Password1!"
    )

    fun validMinLengthHint(length: Int) = "must have at least $length characters"
    fun validMaxLengthHint(length: Int) = "must have at most $length characters"

    test("validation should pass for valid request") {
        val result = PostRegisterUserRequestValidation.validate(validRequest())

        result.isValid shouldBe true
        result.errors.isEmpty() shouldBe true
    }

    test("validation should fail for invalid email") {
        val request = validRequest().copy(email = "invalid")

        val result = PostRegisterUserRequestValidation.validate(request)

        result.isValid shouldBe false
        result.errors.messagesAtPath(PostRegisterUserRequest::email) shouldBe listOf(PostRegisterUserRequestValidation.VALID_EMAIL_PATTERN_HINT)
    }

    test("validation should fail for username too short") {
        val request = validRequest().copy(username = "ab")

        val result = PostRegisterUserRequestValidation.validate(request)

        result.isValid shouldBe false
        result.errors.messagesAtPath(PostRegisterUserRequest::username) shouldBe listOf(validMinLengthHint(3))
    }

    test("validation should fail for username too long") {
        val request = validRequest().copy(username = "abcdefghijklmnopqrstuvwxyz1234567890")

        val result = PostRegisterUserRequestValidation.validate(request)

        result.isValid shouldBe false
        result.errors.messagesAtPath(PostRegisterUserRequest::username) shouldBe listOf(validMaxLengthHint(30))
    }

    test("validation should fail for invalid username characters") {
        val request = validRequest().copy(username = "bad@name")

        val result = PostRegisterUserRequestValidation.validate(request)

        result.isValid shouldBe false
        result.errors.messagesAtPath(PostRegisterUserRequest::username) shouldBe listOf(PostRegisterUserRequestValidation.VALID_USERNAME_PATTERN_HINT)
    }

    test("validation should fail for too short password") {
        val request = validRequest().copy(password = "P1p!")

        val result = PostRegisterUserRequestValidation.validate(request)

        result.isValid shouldBe false
        result.errors.messagesAtPath(PostRegisterUserRequest::password) shouldBe listOf(validMinLengthHint(8))

    }

    test("validation should fail for weak password") {
        val request = validRequest().copy(password = "weakpass")

        val result = PostRegisterUserRequestValidation.validate(request)

        result.isValid shouldBe false
        result.errors.messagesAtPath(PostRegisterUserRequest::password) shouldBe listOf(PostRegisterUserRequestValidation.VALID_PASSWORD_PATTERN_HINT)

    }

    test("validation should report multiple field errors") {
        val request = PostRegisterUserRequest(
            username = "john#",
            email = "bad@com",
            password = "123"
        )

        val result = PostRegisterUserRequestValidation.validate(request)

        result.isValid shouldBe false

        result.errors.messagesAtPath(PostRegisterUserRequest::email) shouldBe listOf(PostRegisterUserRequestValidation.VALID_EMAIL_PATTERN_HINT)
        result.errors.messagesAtPath(PostRegisterUserRequest::username) shouldBe listOf(PostRegisterUserRequestValidation.VALID_USERNAME_PATTERN_HINT)
        result.errors.messagesAtPath(PostRegisterUserRequest::password) shouldBe listOf(validMinLengthHint(8),PostRegisterUserRequestValidation.VALID_PASSWORD_PATTERN_HINT)

    }
})