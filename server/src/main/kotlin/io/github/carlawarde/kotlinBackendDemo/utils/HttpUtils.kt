package io.github.carlawarde.kotlinBackendDemo.utils

import io.ktor.server.application.ApplicationCall

object HttpUtils {

    fun isFailedCall(call: ApplicationCall): Boolean {
        call.response.status()?.value?.let {
            return it > 399
        }
        return false
    }
}