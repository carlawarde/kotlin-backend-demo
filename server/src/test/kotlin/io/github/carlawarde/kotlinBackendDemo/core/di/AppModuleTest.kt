package io.github.carlawarde.kotlinBackendDemo.core.di

import io.github.carlawarde.kotlinBackendDemo.infrastructure.config.DatabaseConfig
import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.DatabaseManager
import io.kotest.core.spec.style.FunSpec
import io.micrometer.core.instrument.MeterRegistry
import io.mockk.mockk
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
class AppModuleTest: FunSpec({

    test("koin graph should be valid") {
        val mockDatabaseManager = mockk<DatabaseManager>()

        appModule(mockDatabaseManager).verify(
            extraTypes = listOf(
                DatabaseConfig::class,
                MeterRegistry::class,
            )
        )
    }

})