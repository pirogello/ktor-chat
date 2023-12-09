package chat.com.example.service

import chat.com.example.dao.MessageDao
import chat.com.example.model.Message

class MessageServiceImpl(
    private val messageDao : MessageDao
) : MessageService {
    override suspend fun getMessagesByChatId(chatId: Int): List<Message> {
        return messageDao.getMessagesByChatId(chatId)
    }

    override suspend fun createMessage(chatId: Int, senderId: Int, content: String): Message {
        return messageDao.createMessage(chatId, senderId, content)
    }
}