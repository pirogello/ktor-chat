package chat.com.example.plugins

import chat.com.example.dao.ChatDaoImpl
import chat.com.example.dao.MessageDaoImpl
import chat.com.example.dao.UserDaoImpl
import chat.com.example.routes.chat
import chat.com.example.routes.login
import chat.com.example.routes.message
import chat.com.example.routes.user
import chat.com.example.service.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRoutes(environment: ApplicationEnvironment) {

    val userDao = UserDaoImpl()
    val chatDao = ChatDaoImpl()
    val messageDao = MessageDaoImpl()
    val userService = UserServiceImpl(userDao)
    val authService = JWTAuthServiceImpl(userDao, environment)
    val chatService = ChatServiceImpl(chatDao, userDao)
    val messageService = MessageServiceImpl(messageDao)

    install(Routing) {
        user(
            userService
        )
        login(
            authService,
            userService
        )
        chat(
            chatService,
            messageService
        )
        message(
            messageService
        )
    }
}
