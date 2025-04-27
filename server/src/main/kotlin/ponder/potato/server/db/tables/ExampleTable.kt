package ponder.potato.server.db.tables

import klutch.db.tables.UserTable
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import ponder.potato.model.data.Example

internal object ExampleTable : LongIdTable("example") {
    val userId = reference("user_id", UserTable, onDelete = ReferenceOption.CASCADE)
    val symtrix = text("symtrix")
}

internal fun ResultRow.toExample() = Example(
    id = this[ExampleTable.id].value,
    userId = this[ExampleTable.userId].value,
    symtrix = this[ExampleTable.symtrix],
)