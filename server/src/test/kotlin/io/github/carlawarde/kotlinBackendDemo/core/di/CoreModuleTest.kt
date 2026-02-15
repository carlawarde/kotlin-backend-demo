package io.github.carlawarde.kotlinBackendDemo.core.di

import io.github.carlawarde.kotlinBackendDemo.infrastructure.db.types.DatabaseProvider
import io.kotest.core.spec.style.FunSpec
import io.mockk.mockk
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
class CoreModuleTest: FunSpec({

    test("koin graph should be valid") {
        val mockDatabaseProvider = mockk<DatabaseProvider>()

        coreModule(mockDatabaseProvider).verify()
    }

})