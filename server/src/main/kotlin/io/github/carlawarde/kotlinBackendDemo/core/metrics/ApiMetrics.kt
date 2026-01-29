package io.github.carlawarde.kotlinBackendDemo.core.metrics

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tags
import io.micrometer.core.instrument.Timer
import java.util.concurrent.TimeUnit
import kotlin.collections.forEach

object ApiMetrics {
    const val API_DURATION_NAME = "api.http.request.duration.seconds"
    const val API_REQUESTS_TOTAL_NAME = "api.http.request.total"

    const val API_NAME_LABEL = "api"
    const val ACTION_NAME_LABEL = "action"
    const val OUTCOME_LABEL = "outcome"

    /**
     * Pre-register all timers and counters for every action and outcome..
     */
    fun build(registry: MeterRegistry) {
        val allTags = findAllTags()

        allTags.forEach { tags ->
            Timer.builder(API_DURATION_NAME)
                .tags(tags)
                .description("Duration of API requests")
                .register(registry)

            Counter.builder(API_REQUESTS_TOTAL_NAME)
                .tags(tags)
                .description("Total number of API requests")
                .register(registry)
        }
    }


    fun recordSuccess(
        registry: MeterRegistry,
        action: ApiAction,
        durationMs: Long
    ) {
        record( registry, action, durationMs, "success")
    }

    fun recordFailure(
        registry: MeterRegistry,
        action: ApiAction,
        durationMs: Long
    ) {
        record(registry, action, durationMs, "failure")
    }

    internal fun findAllTags(): List<Tags> {
        val outcomes = listOf("success", "failure")

        return listOf(
            ReviewAction.Companion.all,
            UserAction.Companion.all,
            GameAction.Companion.all
        ).flatten().flatMap { action ->
            outcomes.map { outcome ->
                Tags.of(
                    API_NAME_LABEL, action.api,
                    ACTION_NAME_LABEL, action.action,
                    OUTCOME_LABEL, outcome
                )
            }
        }
    }

    private fun record(
        registry: MeterRegistry,
        action: ApiAction,
        durationMs: Long,
        outcome: String
    ) {
        val tags = Tags.of(
            API_NAME_LABEL, action.api,
            ACTION_NAME_LABEL, action.action,
            OUTCOME_LABEL, outcome
        )

        registry.timer(API_DURATION_NAME, tags)
            .record(durationMs, TimeUnit.MILLISECONDS)

        registry.counter(API_REQUESTS_TOTAL_NAME, tags)
            .increment()
    }
}