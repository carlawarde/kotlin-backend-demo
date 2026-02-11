package io.github.carlawarde.kotlinBackendDemo.core.metrics


import io.github.carlawarde.kotlinBackendDemo.infrastructure.observability.GameAction
import io.github.carlawarde.kotlinBackendDemo.infrastructure.observability.ReviewAction
import io.github.carlawarde.kotlinBackendDemo.infrastructure.observability.metrics.ApiMetrics
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.Tags
import io.micrometer.core.instrument.Timer
import io.micrometer.core.instrument.simple.SimpleMeterRegistry

class ApiMetricsTest : FunSpec({

    lateinit var registry: SimpleMeterRegistry

    beforeTest {
        registry = SimpleMeterRegistry()
        ApiMetrics.build(registry)
    }

    fun getTimer(tags: Tags): Timer =
        registry.get(ApiMetrics.API_DURATION_NAME).tags(tags).timer()

    fun getCounter(tags: Tags): Counter =
        registry.get(ApiMetrics.API_REQUESTS_TOTAL_NAME).tags(tags).counter()

    test("build should pre-register timers and counters for all actions and outcomes") {
        assertSoftly {
            ApiMetrics.findAllTags().forEach { tags ->
                val timer = getTimer(tags)
                val counter = getCounter(tags)

                withClue("Tags: $tags") {
                    timer.count() shouldBe 0L
                    counter.count() shouldBe 0.0
                }
            }
        }
    }

    test("recordSuccess should increment executions and record duration") {
        val action = ReviewAction.Create
        val tags = Tags.of(
            ApiMetrics.API_NAME_LABEL, action.api,
            ApiMetrics.ACTION_NAME_LABEL, action.action,
            ApiMetrics.OUTCOME_LABEL, "success"
        )

        val timer = getTimer(tags)
        val counter = getCounter(tags)

        val timerBefore = timer.count()
        val counterBefore = counter.count()

        ApiMetrics.recordSuccess( registry, action, 500)

        timer.count() shouldBe timerBefore + 1
        counter.count() shouldBe counterBefore + 1
    }

    test("recordFailure should increment executions and failure outcome") {
        val action = GameAction.Log
        val successTags = Tags.of(
            ApiMetrics.API_NAME_LABEL, action.api,
            ApiMetrics.ACTION_NAME_LABEL, action.action,
            ApiMetrics.OUTCOME_LABEL, "success"
        )
        val failureTags = Tags.of(
            ApiMetrics.API_NAME_LABEL, action.api,
            ApiMetrics.ACTION_NAME_LABEL, action.action,
            ApiMetrics.OUTCOME_LABEL, "failure"
        )

        val counterSuccessBefore = getCounter(successTags).count()
        val counterFailureBefore = getCounter(failureTags).count()

        ApiMetrics.recordFailure(registry, action, 300)

        getCounter(successTags).count() shouldBe counterSuccessBefore
        getCounter(failureTags).count() shouldBe counterFailureBefore + 1
    }

    test("invalid action should not exist in registry") {
        val fakeTags = Tags.of(
            ApiMetrics.API_NAME_LABEL, "reviews",
            ApiMetrics.ACTION_NAME_LABEL, "nonexistent",
            ApiMetrics.OUTCOME_LABEL, "success"
        )

        val timerExists = registry.find(ApiMetrics.API_DURATION_NAME)
            .tags(fakeTags)
            .timer() != null
        val counterExists = registry.find(ApiMetrics.API_REQUESTS_TOTAL_NAME)
            .tags(fakeTags)
            .counter() != null

        timerExists shouldBe false
        counterExists shouldBe false
    }
})