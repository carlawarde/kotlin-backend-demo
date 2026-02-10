package io.github.carlawarde.kotlinBackendDemo.setup

import io.github.carlawarde.kotlinBackendDemo.infrastructure.config.DatabaseConfig

object IntegrationTestBaseConfig {
    private fun asMap(): Map<String, String> =
        with(PostgresTestContainer.container) {
            mapOf(
                "database.host" to host,
                "database.type" to "postgresql",
                "database.port" to firstMappedPort.toString(),
                "database.name" to databaseName,
                "database.user" to username,
                "database.password" to password
            )
        }

    fun getAsDatabaseConfig(): DatabaseConfig =
        DatabaseConfig(
            host = System.getProperty("database.host"),
            type = System.getProperty("database.type"),
            port = System.getProperty("database.port").toInt(),
            name = System.getProperty("database.name"),
            user = System.getProperty("database.user"),
            password = System.getProperty("database.password"),
        )

    fun applyToSystemProperties() {
        asMap().forEach { (key, value) ->
            System.setProperty(key, value)
        }
    }

    fun clearSystemProperties() {
        asMap().keys.forEach(System::clearProperty)
    }

}