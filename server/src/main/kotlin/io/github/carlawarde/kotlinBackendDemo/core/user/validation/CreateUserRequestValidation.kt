package io.github.carlawarde.kotlinBackendDemo.core.user.validation

import io.github.carlawarde.kotlinBackendDemo.core.user.dto.CreateUserRequest
import io.konform.validation.Validation
import io.konform.validation.constraints.maxLength
import io.konform.validation.constraints.minLength
import io.konform.validation.constraints.pattern

object CreateUserRequestValidation {

    val validate = Validation {
        CreateUserRequest::email {
            pattern(".+@.+..+") hint "Please provide a valid email address."
        }

        CreateUserRequest::username {
            minLength(3)
            maxLength(30)
            pattern("^[a-zA-Z0-9_\\-.]*$") hint "Username may only contain alphanumeric characters, periods, hyphens, and underscores."
        }

        CreateUserRequest::password {
            minLength(8)
            pattern("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).+$") hint "Password must contain at least one lowercase letter, one uppercase letter, one number and one special character."
        }
    }
}