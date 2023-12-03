package chat.com.example.routes

import chat.com.example.model.CreateChatRequest
import chat.com.example.model.JoinOrLeaveChatRequest
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
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

fun Route.chat(
    chatService: ChatService,
    messageService: MessageService
) {
    route("/api/chat"){
        val connections = Collections.synchronizedMap<Int, HashSet<Connection>>(HashMap())

        authenticate {

            post("/create"){
                val principalId = call.authentication.principal<JWTPrincipal>()?.payload?.getClaim("id")?.asInt()
                val createChatData = call.receive<CreateChatRequest>()
                if(principalId != createChatData.creatorId)
                    call.respond(HttpStatusCode.BadRequest, "CreatorId don`t equals principalId")
                else {
                    chatService.createChat(createChatData)
                    call.respond(HttpStatusCode.Created)
                }
            }

            post("/join-chat"){
                val joinChatData = call.receive<JoinOrLeaveChatRequest>()
                chatService.joinChat(joinChatData)
                call.respond(HttpStatusCode.OK)
            }

            post("/leave-chat"){
                val joinChatData = call.receive<JoinOrLeaveChatRequest>()
                chatService.leaveChat(joinChatData)
                call.respond(HttpStatusCode.OK)
            }

            webSocket("/room/{chatId}") {
                // check: can user connect to chat with id=chatId
                val principal = call.authentication.principal<JWTPrincipal>()
                val principalId = principal?.payload?.getClaim("id")?.asInt() ?: throw Exception("No principal or claim(id)")
                val principalUsername = principal.payload.getClaim("userName")?.asString()
                val chatId = call.parameters["chatId"]?.toInt() ?: throw IllegalStateException("Numeric ID required")
                if(chatService.getChat(chatId)?.participants?.map { it.id }?.contains(principalId) != true)
                    throw IllegalStateException("User with id $principalId is not join chat with id $chatId")
                // add connection to chat with id=chatId
                if(!connections.containsKey(chatId)) connections[chatId] = HashSet()
                val thisConnection = Connection(this, principalId, principalUsername)
                connections[chatId]?.add(thisConnection)
                try {
                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        val receivedText = frame.readText()
                        // save message to db
                        messageService.createMessage(chatId = chatId, senderId = principalId, content = receivedText)
                        // send message to all users in chat with id=chatId
                        connections[chatId]?.forEach {
                            if(it.session != thisConnection.session)
                                it.session.send("from user($principalUsername): $receivedText\n")
                        }
                    }
                } catch (e: Exception) {
                    println(e.localizedMessage)
                } finally {
                    // remove connection from chat with id=chatId
                    connections[chatId]?.remove(thisConnection)
                }
            }
        }

        get("/{id}"){
            val id = call.parameters["id"]?.toInt() ?: throw IllegalStateException("Numeric ID required")
            val chatResponse = chatService.getChat(id)
            if(chatResponse == null){
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(chatResponse)
            }
        }

    }
}