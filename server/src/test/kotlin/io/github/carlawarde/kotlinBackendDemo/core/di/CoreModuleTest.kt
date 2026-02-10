package io.github.carlawarde.kotlinBackendDemo.core.di

import io.kotest.core.spec.style.FunSpec
import io.mockk.mockk
import org.jetbrains.exposed.v1.jdbc.Database
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
class CoreModuleTest: FunSpec({

    test("koin graph should be valid") {
        val mockDatabase = mockk<Database>()

        coreModule(mockDatabase).verify()
    }

})