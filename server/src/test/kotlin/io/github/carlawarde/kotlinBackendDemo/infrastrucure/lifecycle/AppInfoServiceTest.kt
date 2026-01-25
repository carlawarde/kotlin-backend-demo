package io.github.carlawarde.kotlinBackendDemo.infrastrucure.lifecycle

import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.DatabaseManager
import io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle.AppInfoService
import io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle.State
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class AppInfoServiceTest : FunSpec({

    lateinit var databaseManager: DatabaseManager
    lateinit var service: AppInfoService

    beforeTest {
        databaseManager = mockk()
        service = AppInfoService(databaseManager)
    }

    test("liveness should always be 1.0") {
        service.getLiveness() shouldBe 1.0
    }

    test("readiness should be 1.0 when database is connected") {
        every { databaseManager.isConnected() } returns true

        service.getReadiness() shouldBe 1.0
    }

    test("readiness should be 0.0 when database is not connected") {
        every { databaseManager.isConnected() } returns false

        service.getReadiness() shouldBe 0.0
    }

    test("initial lifecycle state should be STARTING") {
        service.getStatus() shouldBe State.STARTING.name
    }

    test("state should transition from STARTING to RUNNING") {
        service.setStatus(State.RUNNING)

        service.getStatus() shouldBe State.RUNNING.name
    }

    test("state should transition from RUNNING to DRAINING") {
        service.setStatus(State.RUNNING)
        service.setStatus(State.DRAINING)

        service.getStatus() shouldBe State.DRAINING.name
    }

    test("state should transition from DRAINING to STOPPED") {
        service.setStatus(State.RUNNING)
        service.setStatus(State.DRAINING)
        service.setStatus(State.STOPPED)

        service.getStatus() shouldBe State.STOPPED.name
    }

    test("state should not allow skipping lifecycle states") {
        service.setStatus(State.STOPPED)

        service.getStatus() shouldBe State.STARTING.name
    }

    test("state should not allow RUNNING -> STOPPED directly") {
        service.setStatus(State.RUNNING)
        service.setStatus(State.STOPPED)

        service.getStatus() shouldBe State.RUNNING.name
    }
})