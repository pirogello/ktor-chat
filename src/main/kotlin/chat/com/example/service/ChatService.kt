package chat.com.example.service

import chat.com.example.model.ChatWithParticipantsResponse
import chat.com.example.model.CreateChatRequest
import chat.com.example.model.JoinOrLeaveChatRequest
import chat.com.example.model.UserResponse
import io.ktor.server.auth.jwt.*

interface ChatService {
    suspend fun createChat(dto: CreateChatRequest)
    suspend fun joinChat(dto: JoinOrLeaveChatRequest)
    suspend fun leaveChat(dto: JoinOrLeaveChatRequest)
    suspend fun getChat(id: Int): ChatWithParticipantsResponse?
    suspend fun getUserFromChat(principal: JWTPrincipal, chatId: Int): UserResponse?
}