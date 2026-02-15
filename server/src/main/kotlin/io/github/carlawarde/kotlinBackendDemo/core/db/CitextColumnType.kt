package io.github.carlawarde.kotlinBackendDemo.core.db

import org.jetbrains.exposed.v1.core.ColumnType
import org.jetbrains.exposed.v1.core.Table

class CitextColumnType : ColumnType<String>() {
    override fun sqlType(): String = "CITEXT"

    override fun valueFromDB(value: Any): String {
        return value.toString()
    }
}

fun Table.citext(name: String) =
    registerColumn(name, CitextColumnType())