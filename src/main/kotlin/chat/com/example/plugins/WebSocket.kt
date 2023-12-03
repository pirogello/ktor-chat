package chat.com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.websocket.*

fun Application.configureWebSocket() {
    install(WebSockets) {
//        pingPeriod = Duration.ofSeconds(15)
//        timeout = Duration.ofSeconds(15)
//        maxFrameSize = Long.MAX_VALUE
//        masking = false
    }

}
