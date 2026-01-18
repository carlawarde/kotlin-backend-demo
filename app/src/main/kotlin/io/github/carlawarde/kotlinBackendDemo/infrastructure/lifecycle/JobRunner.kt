package io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.io.IOException
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class JobRunner(private val registry: MeterRegistry,parentContext: CoroutineContext = Dispatchers.Default) {
    private val logger = LoggerFactory.getLogger(JobRunner::class.java)

    private val state = AtomicReference(State.RUNNING)

    private val runningJobs = AtomicInteger(0)

    private val rootJob = SupervisorJob()
    private val scope = CoroutineScope(parentContext + rootJob)

    private val jobRunningGauge = registry.gauge("job.running", runningJobs)!!

    fun submit(type: String = "default", block: suspend CoroutineScope.() -> Unit): Boolean {
        if (state.get() != State.RUNNING) return false

        val errorsCounter = registry.counter("job_errors_total", "job", type)
        logger.info("Starting async job $type...")
        scope.launch {
            val startTime = System.currentTimeMillis()
            runningJobs.incrementAndGet()
            jobRunningGauge.incrementAndGet()
            try {
                block()
            }
            catch (e: Throwable) {
                errorsCounter.increment()
                logger.error("Job '$type' failed", e)
                throw e
            }
            finally {
                val duration = System.currentTimeMillis() - startTime
                registry.timer("jobs_duration", "type", type)
                    .record(duration, TimeUnit.MILLISECONDS)
                registry.counter("jobs_completed", "type", type).increment()
                runningJobs.decrementAndGet()
                logger.info("Finished job $type...")
            }
        }

        return true
    }

    fun submitPeriodicJob(
        type: String,
        interval: Duration = 1.minutes,
        recoverableExceptions: Set<KClass<out Throwable>> = setOf(IOException::class),
        block: suspend CoroutineScope.() -> Unit
    ) {
        val errorCounter = registry.counter("job_errors_total", "job", type)

        submit(type) {
            while (isActive) {
                try {
                    block()
                } catch (e: Throwable) {
                    when {
                        recoverableExceptions.any { it.isInstance(e) } -> {
                            logger.warn("Recoverable error in periodic job $type", e)
                            errorCounter.increment()
                        }
                        else -> throw e
                    }
                }
                delay(interval.inWholeMilliseconds)
            }
        }
    }

    fun runSync(type: String = "default", block: () -> Unit): Boolean {
        if (state.get() != State.RUNNING) return false

        val errorsCounter = registry.counter("job_errors_total", "job", type)

        logger.info("Starting sync job $type...")
        val startTime = System.currentTimeMillis()
        runningJobs.incrementAndGet()
        jobRunningGauge.incrementAndGet()
        try {
            block()
        }
        catch (e: Throwable) {
            errorsCounter.increment()
            logger.error("Job '$type' failed", e)
            throw e
        }
        finally {
            val duration = System.currentTimeMillis() - startTime
            registry.timer("jobs.duration", "type", type)
                .record(duration, TimeUnit.MILLISECONDS)
            registry.counter("jobs.completed", "type", type).increment()
            runningJobs.decrementAndGet()
            logger.info("Finished job $type...")
        }

        return true
    }

    fun shutdown(timeout: Duration = 10.seconds) {
        runBlocking {
            if (!state.compareAndSet(State.RUNNING, State.SHUTTING_DOWN)) {
                return@runBlocking
            }

            withTimeoutOrNull(timeout.inWholeMilliseconds) {
                while (runningJobs.get() > 0) {
                    delay(50)
                }
            }

            rootJob.cancelAndJoin()
            state.set(State.STOPPED)
        }
    }

    fun runningJobCount(): Int = runningJobs.get()
}
