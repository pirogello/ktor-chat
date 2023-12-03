package chat.com.example.util

import io.ktor.websocket.*

class Connection(val session: DefaultWebSocketSession, val userId: Int, val username: String?) {

}