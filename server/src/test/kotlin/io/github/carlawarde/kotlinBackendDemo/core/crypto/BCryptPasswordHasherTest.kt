package io.github.carlawarde.kotlinBackendDemo.core.crypto

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class BCryptPasswordHasherTest : FunSpec({

    val hasher = BcryptPasswordHasher(cost = 4)

    test("hash should produce a non-empty string") {
        val hash = hasher.hash("SuperSecret123!")
        hash.isNotEmpty() shouldBe true
    }

    test("verify should return true for the correct password") {
        val password = "SuperSecret123!"
        val hash = hasher.hash(password)
        hasher.verify(password, hash) shouldBe true
    }

    test("verify should return false for an incorrect password") {
        val password = "SuperSecret123!"
        val hash = hasher.hash(password)
        hasher.verify("WrongPassword!", hash) shouldBe false
    }

    test("hashes of the same password should be different due to salt") {
        val password = "SuperSecret123!"
        val hash1 = hasher.hash(password)
        val hash2 = hasher.hash(password)
        hash1 shouldBe hash1
        (hash1 == hash2) shouldBe false
    }

    test("verify throws no exception for empty password input") {
        val password = ""
        val hash = hasher.hash(password)
        hasher.verify(password, hash) shouldBe true
        hasher.verify("something", hash) shouldBe false
    }

    test("hashing should throw IllegalArgumentException on extremely long password") {
        val longPassword = "a".repeat(100_000)

        shouldThrow<IllegalArgumentException> {
            hasher.hash(longPassword)
        }
    }
})