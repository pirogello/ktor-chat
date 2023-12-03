package chat.com.example.dao

import chat.com.example.model.ChatWithParticipantsResponse
import chat.com.example.model.UserResponse
import chat.com.example.model.table.Chats
import chat.com.example.model.table.Users
import chat.com.example.model.table.UsersInChats
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class ChatDaoImpl: ChatDao {

    private fun rowToUserResponse(row: ResultRow) = UserResponse(
        id = row[Users.id],
        username = row[Users.username]
    )
    override suspend fun createChat(name: String, creatorId: Int) = query {
        val chatId = Chats.insert {
            it[Chats.name] = name
            it[Chats.adminId] =  creatorId
        } get Chats.id

        val userInChat = UsersInChats.insert {
            it[UsersInChats.chatId] = chatId
            it[UsersInChats.userId] = creatorId
        }
    }

    override suspend fun joinChat(chatId: Int, userId: Int) = query {
        val userInChat = UsersInChats.insert {
            it[UsersInChats.chatId] = chatId
            it[UsersInChats.userId] = userId
        }
    }

    override suspend fun leaveChat(chatId: Int, userId: Int) = query{
        val userInChat = UsersInChats
            .deleteWhere {UsersInChats.userId eq userId}
    }

    override suspend fun getChat(chatId: Int): ChatWithParticipantsResponse = query{
        val chat = Chats.select(Chats.id eq chatId).singleOrNull() ?: throw Exception("Chat with Id $chatId not found")
        val participants = (UsersInChats leftJoin Users)
            .slice(Users.id, Users.username)
            .select(UsersInChats.chatId eq chatId)
            .map{ rowToUserResponse(it)}

        ChatWithParticipantsResponse(
            id = chat[Chats.id],
            name = chat[Chats.name],
            adminId = chat[Chats.adminId],
            participants = participants
        )
    }
}