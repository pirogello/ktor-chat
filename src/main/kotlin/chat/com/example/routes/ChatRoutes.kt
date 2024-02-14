package chat.com.example.routes

import chat.com.example.model.CreateChatRequest
import chat.com.example.model.JoinOrLeaveChatRequest
import chat.com.example.model.Message
import chat.com.example.model.UserResponse
import chat.com.example.service.ChatService
import chat.com.example.service.MessageService
import chat.com.example.util.Connection
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json
import java.util.*

fun Route.chat(
    chatService: ChatService,
    messageService: MessageService
) {
    route("/api/chat") {
        val connections = Collections.synchronizedMap<Int, HashSet<Connection>>(HashMap())

        suspend fun createMessageAndGetJson(chatId: Int, userId: Int, content: String): String = coroutineScope {
            val message = async {
                messageService.createMessage(
                    chatId = chatId,
                    senderId = userId,
                    content = content
                )
            }
            val retM = message.await()
            return@coroutineScope Json.encodeToString(Message.serializer(), retM)
        }
        fun addUserToChatConnectionPool(session: DefaultWebSocketSession, user: UserResponse, chatId: Int): Connection {
            val curUserConnection = Connection(session, user.id, user.username)
            if (!connections.containsKey(chatId)) connections[chatId] = HashSet()
            connections[chatId]?.add(curUserConnection)
            return curUserConnection
        }
        fun removeUserFromChatConnectionPool(connection: Connection, chatId: Int){
            connections[chatId]?.remove(connection)
        }
        suspend fun sendPrevMessagesToNewUserConnection(connection: Connection, chatId: Int) {
            val prevMessages = messageService.getMessagesByChatId(chatId)
            for (m in prevMessages) {
                val jsonMessage = Json.encodeToString(Message.serializer(), m)
                connection.session.send(jsonMessage)
            }
        }
        suspend fun sendMessage(connection: Connection, chatId: Int, content: String ){
            connections[chatId]?.forEach {
                //if (it.session != connection.session)
                it.session.send(content)
            }
        }

        authenticate {

            post("/create") {
                val principalId = call.authentication.principal<JWTPrincipal>()?.payload?.getClaim("id")?.asInt()
                val createChatData = call.receive<CreateChatRequest>()
                if (principalId != createChatData.creatorId)
                    call.respond(HttpStatusCode.BadRequest, "CreatorId don`t equals principalId")
                else {
                    chatService.createChat(createChatData)
                    call.respond(HttpStatusCode.Created)
                }
            }

            post("/join-chat") {
                val joinChatData = call.receive<JoinOrLeaveChatRequest>()
                chatService.joinChat(joinChatData)
                call.respond(HttpStatusCode.OK)
            }

            post("/leave-chat") {
                val joinChatData = call.receive<JoinOrLeaveChatRequest>()
                chatService.leaveChat(joinChatData)
                call.respond(HttpStatusCode.OK)
            }

            webSocket("/room/{chatId}") {
                val principal = call.authentication.principal<JWTPrincipal>() ?: throw IllegalStateException("Not a principal")
                val chatId = call.parameters["chatId"]?.toInt() ?: throw IllegalStateException("Numeric ID required")
                val user = chatService.getUserFromChat(principal, chatId)
                user?.let {
                    val curUserConnection = addUserToChatConnectionPool(this, user, chatId)
                    sendPrevMessagesToNewUserConnection(curUserConnection, chatId)
                    try {
                        for (frame in incoming) {
                            frame as? Frame.Text ?: continue
                            val messageJson = createMessageAndGetJson(chatId, user.id, frame.readText())
                            sendMessage(curUserConnection, chatId, messageJson)
                        }
                    } catch (e: Exception) {
                        println(e.localizedMessage)
                    } finally {
                        removeUserFromChatConnectionPool(curUserConnection, chatId)
                    }
                } ?: {
                    //todo Process case when user is not in chat
                }
                Unit
            }
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalStateException("Numeric ID required")
            val chatResponse = chatService.getChat(id)
            if (chatResponse == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(chatResponse)
            }
        }


    }
}
