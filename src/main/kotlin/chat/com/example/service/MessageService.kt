package chat.com.example.service

import chat.com.example.model.Message

interface MessageService {
    suspend fun getMessagesByChatId(chatId: Int): List<Message>
    suspend fun createMessage(chatId: Int, senderId: Int, content: String): Message
}