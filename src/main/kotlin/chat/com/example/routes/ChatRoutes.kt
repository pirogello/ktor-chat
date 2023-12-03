package chat.com.example.routes

import chat.com.example.model.CreateChatRequest
import chat.com.example.model.JoinOrLeaveChatRequest
import chat.com.example.service.ChatService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.chat(
    chatService: ChatService
) {
    route("/api/chat"){

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