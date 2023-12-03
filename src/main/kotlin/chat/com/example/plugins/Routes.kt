package chat.com.example.plugins

import chat.com.example.dao.UserDaoImpl
import chat.com.example.routes.login
import chat.com.example.routes.user
import chat.com.example.service.AuthService
import chat.com.example.service.JWTAuthServiceImpl
import chat.com.example.service.UserServiceImpl
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRoutes(environment: ApplicationEnvironment) {

    val userDao = UserDaoImpl()
    val userService = UserServiceImpl(userDao)
    val authService = JWTAuthServiceImpl(userDao, environment)

    install(Routing) {
        user(
            userService
        )
        login(
            authService,
            userService
        )

    }
}
