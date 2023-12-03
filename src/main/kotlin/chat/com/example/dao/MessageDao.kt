package chat.com.example.dao

import chat.com.example.model.Message

interface MessageDao {
    suspend fun getMessagesByChatId(chatId: Int): List<Message>
    suspend fun createMessage(chatId: Int, senderId: Int, content: String)
}