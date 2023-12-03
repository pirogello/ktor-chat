package chat.com.example.service

import chat.com.example.dao.ChatDao
import chat.com.example.dao.UserDao
import chat.com.example.model.ChatWithParticipantsResponse
import chat.com.example.model.CreateChatRequest
import chat.com.example.model.JoinOrLeaveChatRequest

class ChatServiceImpl(
    private val chatDao: ChatDao,
    private val userDao: UserDao
): ChatService {
    override suspend fun createChat(dto: CreateChatRequest) {
        userDao.getUser(dto.creatorId) ?: throw Exception("User with ${dto.creatorId} not found")
        chatDao.createChat(dto.name, dto.creatorId)
    }

    override suspend fun joinChat(dto: JoinOrLeaveChatRequest) {
        userDao.getUser(dto.userId) ?: throw Exception("User with Id ${dto.userId} not found")
        chatDao.getChat(dto.chatId) ?: throw Exception("Chat with Id ${dto.chatId} not found")
        chatDao.joinChat(userId = dto.userId, chatId = dto.chatId)
    }

    override suspend fun leaveChat(dto: JoinOrLeaveChatRequest) {
        userDao.getUser(dto.userId) ?: throw Exception("User with Id ${dto.userId} not found")
        chatDao.getChat(dto.chatId) ?: throw Exception("Chat with Id ${dto.chatId} not found")
        chatDao.leaveChat(userId = dto.userId, chatId = dto.chatId)
    }

    override suspend fun getChat(id: Int): ChatWithParticipantsResponse? {
        return chatDao.getChat(id)
    }
}