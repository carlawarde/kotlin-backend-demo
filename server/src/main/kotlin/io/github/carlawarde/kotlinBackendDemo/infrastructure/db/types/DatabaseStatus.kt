package io.github.carlawarde.kotlinBackendDemo.infrastructure.db.types

interface DatabaseStatus {
    fun isReady(): Boolean
    fun isConnected(): Boolean
}