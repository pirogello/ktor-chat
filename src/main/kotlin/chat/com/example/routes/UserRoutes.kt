package chat.com.example.routes

import chat.com.example.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.user(
    userService: UserService
) {
    route("/api/users") {
        get {
            call.respond(userService.getUsers())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalStateException("Numeric ID required")
            val user = userService.getUserById(id)
            if (user == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(user)
            }
        }

//        post {
//            val dto = call.receive<CreateUserRequest>()
//            try {
//                userService.createUser(dto)
//                call.respond(HttpStatusCode.Created)
//            } catch (e: Exception) {
//                call.respond(HttpStatusCode.BadRequest, e.message.toString())
//            }
//        }
    }


}