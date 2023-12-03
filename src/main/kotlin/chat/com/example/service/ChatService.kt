package chat.com.example.service

import chat.com.example.model.ChatWithParticipantsResponse
import chat.com.example.model.CreateChatRequest
import chat.com.example.model.JoinOrLeaveChatRequest

interface ChatService {
    suspend fun createChat(dto: CreateChatRequest)
    suspend fun joinChat(dto: JoinOrLeaveChatRequest)
    suspend fun leaveChat(dto: JoinOrLeaveChatRequest)
    suspend fun getChat(id: Int): ChatWithParticipantsResponse?
}