package io.github.carlawarde.kotlinBackendDemo.infrastructure.monitoring

data class MetricsContext(
    val service: String,
    val environment: String,
    val region: String,
    val instance: String
)
