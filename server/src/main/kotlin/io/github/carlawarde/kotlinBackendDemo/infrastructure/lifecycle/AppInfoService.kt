package io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle

import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.DatabaseManager
import java.util.concurrent.atomic.AtomicReference


class AppInfoService(private val databaseManager: DatabaseManager) {
    private val appState = AtomicReference(State.STARTING)

    fun getLiveness(): Double = 1.0

    fun getReadiness(): Double {
        return if(databaseManager.isConnected()) 1.0 else 0.0
    }

    fun getStatus(): String = appState.get().name

    fun setStatus(state: State) {
        when(state) {
            State.RUNNING -> appState.compareAndSet(State.STARTING, state)
            State.DRAINING -> appState.compareAndSet(State.RUNNING, state)
            State.STOPPED -> appState.compareAndSet(State.DRAINING, state)
            else -> throw IllegalArgumentException("Illegal state: $state")
        }
    }
}