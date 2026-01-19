package io.github.carlawarde.kotlinBackendDemo.infrastructure.config

data class DatabaseConfig(
    val type: String,
    val host: String,
    val port: Int,
    val name: String,
    val user: String,
    val password: String
) {
    val jdbcUrl: String
        get() = "jdbc:$type://$host:$port/$name"
}