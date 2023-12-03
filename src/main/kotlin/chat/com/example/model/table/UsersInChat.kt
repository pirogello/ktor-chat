package chat.com.example.model.table

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object UsersInChats : Table(name = "t_users_chat") {
    val chatId = reference("chat_id", Chats.id)
    val userId = reference("user_id", Users.id)

    override val primaryKey = PrimaryKey(chatId, userId)
}
