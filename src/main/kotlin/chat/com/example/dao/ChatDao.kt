package chat.com.example.dao

import chat.com.example.model.ChatWithParticipantsResponse

interface ChatDao {
    suspend fun createChat(name: String, creatorId: Int)
    suspend fun joinChat(chatId: Int, userId: Int)
    suspend fun leaveChat(chatId: Int, userId: Int)
    suspend fun getChat(chatId: Int): ChatWithParticipantsResponse?
}