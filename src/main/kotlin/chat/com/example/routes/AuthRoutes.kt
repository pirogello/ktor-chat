package chat.com.example.routes

import chat.com.example.model.CreateUserRequest
import chat.com.example.model.LoginRequest
import chat.com.example.service.AuthService
import chat.com.example.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.login(
    authService: AuthService,
    userService: UserService
) {
    route("/api/auth"){

        post("/login"){
            val loginData = call.receive<LoginRequest>()
            try {
                val resp = authService.generateToken(loginData.username, loginData.password)
                call.respond(HttpStatusCode.OK, resp)
            } catch (e: Exception){
                call.respond(HttpStatusCode.BadRequest, e.message.toString())
            }


        }

        post("/register"){
            val registerData = call.receive<CreateUserRequest>()
            userService.createUser(registerData)
            call.respond(HttpStatusCode.Created)
        }
    }
}