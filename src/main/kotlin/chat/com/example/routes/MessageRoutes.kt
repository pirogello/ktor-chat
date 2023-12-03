package chat.com.example.routes

import chat.com.example.model.CreateUserRequest
import chat.com.example.service.MessageService
import chat.com.example.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.message(
    messageService: MessageService
) {
    route("/api/message") {
        get("chat/{chatId}") {
            val chatId = call.parameters["chatId"]?.toInt() ?: throw IllegalStateException("Numeric ID required")
            call.respond(messageService.getMessagesByChatId(chatId))
        }
    }


}