package io.github.carlawarde.kotlinBackendDemo.infrastructure

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class ApplicationCoroutineScope(
    parentContext: CoroutineContext = Dispatchers.Default
) : CoroutineScope {

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext =
        parentContext + job

    fun shutdown(timeout: Duration = 10.seconds) {
        runBlocking {
            withTimeoutOrNull(timeout) {
                job.cancelAndJoin()
            }
        }
    }
}