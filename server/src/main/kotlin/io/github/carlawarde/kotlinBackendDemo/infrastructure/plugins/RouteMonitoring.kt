package io.github.carlawarde.kotlinBackendDemo.infrastructure.plugins

import io.github.carlawarde.kotlinBackendDemo.core.metrics.ApiMetrics
import io.github.carlawarde.kotlinBackendDemo.core.metrics.ApiAction
import io.github.carlawarde.kotlinBackendDemo.utils.HttpUtils.isFailedCall
import io.ktor.server.application.*
import io.ktor.server.application.hooks.CallFailed
import io.ktor.server.request.path
import io.ktor.util.*
import io.micrometer.core.instrument.MeterRegistry

class RouteMetricsConfig {
    lateinit var registry: MeterRegistry
    val ignoredPaths: List<String> = listOf("/metrics", "/health")
}

val RouteMetricsPlugin = createApplicationPlugin(
    name = "RouteMetrics",
    createConfiguration = ::RouteMetricsConfig
) {
    val registry = pluginConfig.registry
    val ignoredPaths = pluginConfig.ignoredPaths
    val startTimeKey = AttributeKey<Long>("RouteMetrics.startTime")
    val actionKey = AttributeKey<ApiAction>("RouteMetrics.action")

    onCall { call ->
        if (call.request.path() !in ignoredPaths) {
            call.attributes.put(startTimeKey, System.nanoTime())
        }
    }

    onCallRespond { call ->
        if (call.request.path() in ignoredPaths) return@onCallRespond
        val startTime = call.attributes.getOrNull(startTimeKey) ?: return@onCallRespond
        val action = call.attributes.getOrNull(actionKey) ?: return@onCallRespond

        val durationNs = System.nanoTime() - startTime

        if(isFailedCall(call)) {
            ApiMetrics.recordFailure(registry, action, durationNs)
        } else {
            ApiMetrics.recordSuccess(registry, action, durationNs)
        }
    }
}