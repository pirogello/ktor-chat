package chat.com.example.model.table

import chat.com.example.util.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object Messages : Table(name = "t_messages") {
    val id = integer("id").autoIncrement()
    val chatId = reference("chat_id", Chats.id).uniqueIndex()
    val senderId = reference("sender_id", Users.id).uniqueIndex()
    val content = varchar("content", length = 250)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(id)
}


