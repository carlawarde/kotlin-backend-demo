package io.github.carlawarde.kotlinBackendDemo.utils

import io.github.carlawarde.kotlinBackendDemo.core.errors.CoreError
import io.github.carlawarde.kotlinBackendDemo.core.errors.ForbiddenError
import io.github.carlawarde.kotlinBackendDemo.core.errors.UnauthorizedError
import io.github.carlawarde.kotlinBackendDemo.core.errors.ValidationError
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall

object HttpUtils {

    fun isFailedCall(call: ApplicationCall): Boolean {
        call.response.status()?.value?.let {
            return it > 399
        }
        return false
    }

    fun CoreError.toHttpStatus(): HttpStatusCode =
        when (this) {
            is UnauthorizedError -> HttpStatusCode.Unauthorized
            is ForbiddenError -> HttpStatusCode.Forbidden
            is ValidationError -> HttpStatusCode.BadRequest
        }
}