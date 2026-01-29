package io.github.carlawarde.kotlinBackendDemo.setup

import org.testcontainers.postgresql.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

class PostgresTestContainer() {
    companion object {
        val container = PostgreSQLContainer(DockerImageName.parse("postgres:16-alpine")).apply {
            withDatabaseName("test_db")
            withUsername("test")
            withPassword("test")
        }
    }
}