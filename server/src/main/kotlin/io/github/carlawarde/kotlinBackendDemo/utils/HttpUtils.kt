package io.github.carlawarde.kotlinBackendDemo.utils

import io.ktor.server.application.ApplicationCall

object HttpUtils {

    fun isFailedCall(call: ApplicationCall): Boolean {
        call.response.status()?.value?.let {
            if(it > 399) {
                return true
            } else {
                return false
            }
        }
        return false
    }
}