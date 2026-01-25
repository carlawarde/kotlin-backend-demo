package io.github.carlawarde.kotlinBackendDemo.setup

object IntegrationTestConfig {
    val init: Lazy<Unit> = lazy {
        with(PostgresTestContainer.container) {
            System.setProperty("database.host", host)
            System.setProperty("database.port", firstMappedPort.toString())
            System.setProperty("database.name", databaseName)
            System.setProperty("database.user", username)
            System.setProperty("database.password", password)
        }
    }

    fun initialize() {
        init.value
    }

    private val keys = listOf(
        "database.host",
        "database.port",
        "database.name",
        "database.user",
        "database.password"
    )

    fun clear() {
        keys.forEach(System::clearProperty)
    }

}