package chat.com.example.model.table

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object Chats : Table(name = "t_chat") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", length = 50)
    val adminId = reference("admin_id", Users.id)

    override val primaryKey = PrimaryKey(id)
}
