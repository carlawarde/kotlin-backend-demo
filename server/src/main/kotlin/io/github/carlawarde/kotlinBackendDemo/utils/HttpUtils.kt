package io.github.carlawarde.kotlinBackendDemo.utils

import io.github.carlawarde.kotlinBackendDemo.core.errors.AuthError
import io.github.carlawarde.kotlinBackendDemo.core.errors.RequestError
import io.github.carlawarde.kotlinBackendDemo.core.errors.RequestValidationError
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall

object HttpUtils {

    fun isFailedCall(call: ApplicationCall): Boolean {
        call.response.status()?.value?.let {
            return it > 399
        }
        return false
    }

    fun RequestError.toHttpStatus(): HttpStatusCode =
        when (this) {
            is AuthError.UnauthorizedError -> HttpStatusCode.Unauthorized
            is AuthError.ForbiddenError -> HttpStatusCode.Forbidden
            is RequestValidationError -> HttpStatusCode.BadRequest
        }
}