package chat.com.example

import chat.com.example.dao.DbFactory
import chat.com.example.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {

    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    configureSerialization()
    configureWebSocket()
    configureAuthentication(environment)
    configureRoutes(environment)
    configureCors()
    DbFactory.init(environment)

}
