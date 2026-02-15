package io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle

enum class AppState {
    STARTING,
    RUNNING,
    DRAINING,
    STOPPED
}