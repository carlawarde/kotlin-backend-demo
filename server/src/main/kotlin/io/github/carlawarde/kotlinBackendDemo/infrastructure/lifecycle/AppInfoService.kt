package io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle

import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.types.DatabaseStatus
import java.util.concurrent.atomic.AtomicReference


class AppInfoService(private val databaseStatus: DatabaseStatus) {
    private val appState = AtomicReference(AppState.STARTING)

    fun getLiveness(): Double = 1.0

    fun getReadiness(): Double {
        return if(databaseStatus.isConnected()) 1.0 else 0.0
    }

    fun getStatus(): String = appState.get().name

    fun setStatus(newAppState: AppState) {
        when(newAppState) {
            AppState.RUNNING -> appState.compareAndSet(AppState.STARTING, newAppState)
            AppState.DRAINING -> appState.compareAndSet(AppState.RUNNING, newAppState)
            AppState.STOPPED -> appState.compareAndSet(AppState.DRAINING, newAppState)
            else -> throw IllegalArgumentException("Illegal state: $newAppState")
        }
    }
}