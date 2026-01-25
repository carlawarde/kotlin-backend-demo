package io.github.carlawarde.kotlinBackendDemo.infrastructure.plugins

import io.github.carlawarde.kotlinBackendDemo.infrastructure.monitoring.MetricScope
import io.ktor.server.application.*
import io.ktor.server.application.hooks.CallFailed
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.util.*
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import java.util.concurrent.TimeUnit

class RouteMetricsConfig {
    lateinit var registry: MeterRegistry
}

val RouteMetricsPlugin = createApplicationPlugin(
    name = "RouteMetrics",
    createConfiguration = ::RouteMetricsConfig
) {
    val registry = pluginConfig.registry
    val ignoredPaths = listOf("/metrics", "/health")

    onCall { call ->
        if (call.request.path() in ignoredPaths) return@onCall

        call.attributes.put(AttributeKey("startTime"), System.nanoTime())
    }

    onCallRespond { call ->
        if (call.request.path() in ignoredPaths) return@onCallRespond

        val startTime = call.attributes.getOrNull(AttributeKey<Long>("startTime")) ?: return@onCallRespond
        val duration = System.nanoTime() - startTime

        val component = call.attributes.getOrNull(AttributeKey<String>("component")) ?: call.request.path()

        val tags = listOf(
            Tag.of("scope", MetricScope.ROUTE.name),
            Tag.of("component", component),
            Tag.of("operation", call.request.httpMethod.value),
            Tag.of("status", call.response.status()?.value?.toString() ?: "unknown")
        )

        registry.timer("app_operation_duration", tags).record(duration, TimeUnit.NANOSECONDS)
        registry.counter("app_operation_total", tags).increment()
    }

    on(CallFailed) { call, cause ->
        if (call.request.path() in ignoredPaths) return@on

        val component = call.attributes.getOrNull(AttributeKey<String>("component")) ?: call.request.path()

        val tags = listOf(
            Tag.of("scope", MetricScope.ROUTE.name),
            Tag.of("component", component),
            Tag.of("operation", call.request.httpMethod.value),
            Tag.of("error", cause::class.simpleName ?: "unknown")
        )

        registry.counter("app_operation_errors_total", tags).increment()
    }
}