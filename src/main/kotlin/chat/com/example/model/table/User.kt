package chat.com.example.model.table

import org.jetbrains.exposed.sql.Table

object Users : Table(name = "t_users") {
    val id = integer("id").autoIncrement()
    val username = varchar("name", length = 50).uniqueIndex()
    val password = varchar("password", length = 255)

    override val primaryKey = PrimaryKey(id)
}
