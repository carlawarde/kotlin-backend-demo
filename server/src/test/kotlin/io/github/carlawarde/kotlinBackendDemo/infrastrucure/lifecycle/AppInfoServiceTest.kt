package io.github.carlawarde.kotlinBackendDemo.infrastrucure.lifecycle

import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.DatabaseService
import io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle.AppInfoService
import io.github.carlawarde.kotlinBackendDemo.infrastructure.lifecycle.AppState
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class AppInfoServiceTest : FunSpec({

    lateinit var databaseService: DatabaseService
    lateinit var service: AppInfoService

    beforeTest {
        databaseService = mockk()
        service = AppInfoService(databaseService)
    }

    test("liveness should always be 1.0") {
        service.getLiveness() shouldBe 1.0
    }

    test("readiness should be 1.0 when database is connected") {
        every { databaseService.isConnected() } returns true

        service.getReadiness() shouldBe 1.0
    }

    test("readiness should be 0.0 when database is not connected") {
        every { databaseService.isConnected() } returns false

        service.getReadiness() shouldBe 0.0
    }

    test("initial lifecycle state should be STARTING") {
        service.getStatus() shouldBe AppState.STARTING.name
    }

    test("state should transition from STARTING to RUNNING") {
        service.setStatus(AppState.RUNNING)

        service.getStatus() shouldBe AppState.RUNNING.name
    }

    test("state should transition from RUNNING to DRAINING") {
        service.setStatus(AppState.RUNNING)
        service.setStatus(AppState.DRAINING)

        service.getStatus() shouldBe AppState.DRAINING.name
    }

    test("state should transition from DRAINING to STOPPED") {
        service.setStatus(AppState.RUNNING)
        service.setStatus(AppState.DRAINING)
        service.setStatus(AppState.STOPPED)

        service.getStatus() shouldBe AppState.STOPPED.name
    }

    test("state should not allow skipping lifecycle states") {
        service.setStatus(AppState.STOPPED)

        service.getStatus() shouldBe AppState.STARTING.name
    }

    test("state should not allow RUNNING -> STOPPED directly") {
        service.setStatus(AppState.RUNNING)
        service.setStatus(AppState.STOPPED)

        service.getStatus() shouldBe AppState.RUNNING.name
    }
})