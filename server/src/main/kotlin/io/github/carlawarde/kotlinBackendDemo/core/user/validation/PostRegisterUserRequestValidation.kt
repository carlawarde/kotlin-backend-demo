package io.github.carlawarde.kotlinBackendDemo.core.user.validation

import io.github.carlawarde.kotlinBackendDemo.core.user.dto.PostRegisterUserRequest
import io.konform.validation.Validation
import io.konform.validation.constraints.maxLength
import io.konform.validation.constraints.minLength
import io.konform.validation.constraints.pattern

object PostRegisterUserRequestValidation {

    const val VALID_EMAIL_PATTERN_HINT = "provide a valid email address"
    const val VALID_USERNAME_PATTERN_HINT = "may only contain alphanumeric characters, periods, hyphens, and underscores"
    const val VALID_PASSWORD_PATTERN_HINT = "must contain at least one lowercase letter, one uppercase letter, one number and one special character"

    val validate = Validation {
        PostRegisterUserRequest::email {
            pattern(Regex(".+@.+\\..+")) hint VALID_EMAIL_PATTERN_HINT
        }

        PostRegisterUserRequest::username {
            minLength(3)
            maxLength(30)
            pattern(Regex("^[a-zA-Z0-9_\\-.]*$")) hint VALID_USERNAME_PATTERN_HINT
        }

        PostRegisterUserRequest::password {
            minLength(8)
            pattern(Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).+$")) hint VALID_PASSWORD_PATTERN_HINT
        }
    }
}