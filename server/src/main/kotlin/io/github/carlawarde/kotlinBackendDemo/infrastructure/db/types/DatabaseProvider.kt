package io.github.carlawarde.kotlinBackendDemo.infrastructure.db.types

import org.jetbrains.exposed.v1.jdbc.Database

interface DatabaseProvider {
    val db: Database
}