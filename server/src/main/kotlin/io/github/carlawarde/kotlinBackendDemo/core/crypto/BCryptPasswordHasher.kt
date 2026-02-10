package io.github.carlawarde.kotlinBackendDemo.core.crypto

import at.favre.lib.crypto.bcrypt.BCrypt

class BcryptPasswordHasher(
    private val cost: Int = 12
) : PasswordHasher {

    override fun hash(password: String): String {
        return BCrypt.withDefaults()
            .hashToString(cost, password.toCharArray())
    }

    override fun verify(password: String, hash: String): Boolean {
        val result = BCrypt.verifyer().verify(password.toCharArray(), hash)
        return result.verified
    }
}