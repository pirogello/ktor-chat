package chat.com.example

import chat.com.example.dao.DbFactory
import chat.com.example.plugins.configureAuthentication
import chat.com.example.plugins.configureRoutes
import chat.com.example.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {

    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    configureSerialization()
    configureAuthentication(environment)
    configureRoutes(environment)
    DbFactory.init(environment)
}
