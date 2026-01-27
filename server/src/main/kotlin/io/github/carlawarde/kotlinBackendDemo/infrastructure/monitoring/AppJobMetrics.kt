package io.github.carlawarde.kotlinBackendDemo.infrastructure.monitoring

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer

object AppJobMetrics {
    const val APP_JOB_DURATION = "app.job.duration"
    const val APP_JOB_EXECUTIONS = "app.job.executions.total"
    const val APP_JOB_FAILURES = "app.job.failures.total"

    private val JOB_TAGS = arrayOf("job.name", "event.type")

    fun build(registry: MeterRegistry) {
        val duration: Timer = Timer.builder(APP_JOB_DURATION)
            .description("Duration of application jobs")
            .publishPercentileHistogram()
            .tags(*JOB_TAGS)
            .register(registry)

        val executions: Counter = Counter.builder(APP_JOB_EXECUTIONS)
            .description("Total number of job executions")
            .tags(*JOB_TAGS)
            .register(registry)

        val failures: Counter = Counter.builder(APP_JOB_FAILURES)
            .description("Total number of job failures")
            .tags(*JOB_TAGS)
            .register(registry)
    }

}