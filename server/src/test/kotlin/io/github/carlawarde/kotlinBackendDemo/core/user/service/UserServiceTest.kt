package io.github.carlawarde.kotlinBackendDemo.core.user.service

import io.github.carlawarde.kotlinBackendDemo.common.errors.AppException
import io.github.carlawarde.kotlinBackendDemo.core.crypto.PasswordHasher
import io.github.carlawarde.kotlinBackendDemo.core.user.domain.User
import io.github.carlawarde.kotlinBackendDemo.core.user.dto.PostRegisterUserRequest
import io.github.carlawarde.kotlinBackendDemo.core.user.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.time.Instant
import java.util.UUID

class UserServiceTest : FunSpec({

    val mockRepo = mockk<UserRepository>()
    val mockHasher = mockk<PasswordHasher>()
    val userService = UserService(mockRepo, mockHasher)

    test("registerUser should hash password and create user") {
        val request = PostRegisterUserRequest(
            username = "testuser",
            email = "test@example.com",
            password = "ValidPass123!"
        )

        val fakeHash = "hashedPassword"
        val expectedUser = User(
            id = UUID.randomUUID(),
            username = request.username,
            email = request.email.lowercase(),
            passwordHash = fakeHash,
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )

        coEvery { mockHasher.hash(request.password) } returns fakeHash
        coEvery { mockRepo.create(request.username, request.email.lowercase(), fakeHash) } returns expectedUser

        val actual = userService.registerUser(request)

        actual shouldBe expectedUser
        coVerify(exactly = 1) { mockHasher.hash(request.password) }
        coVerify(exactly = 1) { mockRepo.create(request.username, request.email.lowercase(), fakeHash) }
    }

    test("registerUser should throw AppException for invalid input") {
        val invalidRequest = PostRegisterUserRequest(
            username = "u",
            email = "invalid-email",
            password = "123"
        )

        val exception = shouldThrow<AppException> {
            userService.registerUser(invalidRequest)
        }

        exception.error.userMessage shouldBe "User input did not meet requirements. Please resolve the following issues to proceed."
    }
})