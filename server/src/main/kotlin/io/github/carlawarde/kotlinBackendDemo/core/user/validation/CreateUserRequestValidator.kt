package io.github.carlawarde.kotlinBackendDemo.core.user.validation

import io.github.carlawarde.kotlinBackendDemo.core.user.dto.CreateUserRequest
import io.github.carlawarde.kotlinBackendDemo.core.validation.Validator

object CreateUserRequestValidator {

    fun validate(dto: CreateUserRequest) {
        Validator<CreateUserRequest>().apply {
            rule {
                if (it.username.length !in 3..30)
                    "Username must be 3-30 characters."
                else null
            }

            rule {
                if (!it.email.matches(".+@.+\\..+".toRegex()))
                    "Email is invalid."
                else null
            }

            rule {
                if (it.password.length < 8)
                    "Password must be at least 8 characters."
                else null
            }

            rule {
                val regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).+$".toRegex()
                if (!it.password.matches(regex))
                    "Password must contain at least one lowercase letter, one uppercase letter, one number and one special character."
                else null
            }

        }.validate(dto)
    }
}