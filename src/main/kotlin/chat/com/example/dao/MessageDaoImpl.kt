package chat.com.example.dao

import chat.com.example.model.Message
import chat.com.example.model.User
import chat.com.example.model.table.Messages
import chat.com.example.model.table.Users
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class MessageDaoImpl:MessageDao {

    private fun rowToMessage(row: ResultRow) = Message(
        id = row[Messages.id],
        chatId =  row[Messages.chatId],
        senderId = row[Messages.senderId],
        content = row[Messages.content],
        createdAt = row[Messages.createdAt]
    )
    override suspend fun getMessagesByChatId(chatId: Int): List<Message> = query {
        Messages.select(Messages.chatId eq chatId)
            .map { rowToMessage(it) }
    }

    override suspend fun createMessage(chatId: Int, senderId: Int, content: String) {
        Messages.insert {
            it[Messages.chatId] = chatId
            it[Messages.senderId] = senderId
            it[Messages.content] = content
        }
    }
}