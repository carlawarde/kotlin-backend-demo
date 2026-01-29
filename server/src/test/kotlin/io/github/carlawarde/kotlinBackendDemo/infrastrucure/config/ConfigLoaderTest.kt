package io.github.carlawarde.kotlinBackendDemo.infrastrucure.config

import io.github.carlawarde.kotlinBackendDemo.infrastructure.config.loadAppConfig
import io.github.carlawarde.kotlinBackendDemo.infrastructure.config.getIntProperty
import io.github.carlawarde.kotlinBackendDemo.infrastructure.config.getStringProperty
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.server.config.*
import io.mockk.every
import io.mockk.mockk

class ConfigLoaderTest: FunSpec({

    val config = mockk<ApplicationConfig>().apply {
        every { property("ktor.deployment.port").getString() } returns "8080"
        every { property("database.type").getString() } returns "postgresql"
        every { property("database.host").getString() } returns "localhost"
        every { property("database.port").getString() } returns "5432"
        every { property("database.name").getString() } returns "demo_db"
        every { property("database.user").getString() } returns "user"
        every { property("database.password").getString() } returns "pass"

        every { property("metrics.service").getString() } returns "service"
        every { property("metrics.environment").getString() } returns "environment"
        every { property("metrics.region").getString() } returns "region"
        every { property("metrics.instance").getString() } returns "instance"

        every { propertyOrNull("ktor.deployment.port") } returns null
    }

    test("loadAppConfig should parse ApplicationConfig correctly") {
        val appConfig = loadAppConfig(config)

        appConfig.port shouldBe 8080
        with(appConfig.database) {
            type shouldBe "postgresql"
            host shouldBe "localhost"
            port shouldBe 5432
            name shouldBe "demo_db"
            user shouldBe "user"
            password shouldBe "pass"
        }

        with(appConfig.metrics) {
            service shouldBe "service"
            environment shouldBe "environment"
            region shouldBe "region"
            instance shouldBe "instance"
        }
    }

    test("propertyAsString should return property value") {
        val result = config.getStringProperty("database.user")
        result shouldBe "user"
    }

    test("propertyAsInt should return property value") {
        val result = config.getIntProperty("database.port")
        result shouldBe 5432
    }

})