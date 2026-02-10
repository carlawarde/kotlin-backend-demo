package io.github.carlawarde.kotlinBackendDemo.core.crypto

interface PasswordHasher {
    fun hash(password: String): String
    fun verify(password: String, hash: String): Boolean
}