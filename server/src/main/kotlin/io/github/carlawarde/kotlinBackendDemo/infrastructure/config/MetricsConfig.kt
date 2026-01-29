package io.github.carlawarde.kotlinBackendDemo.infrastructure.config

data class MetricsConfig(
    val service: String,
    val environment: String,
    val region: String,
    val instance: String

)
