package io.github.carlawarde.kotlinBackendDemo.infrastructure.db.types

enum class DatabaseState {
    STARTING,
    CONNECTED,
    UNAVAILABLE,
    STOPPED,
}