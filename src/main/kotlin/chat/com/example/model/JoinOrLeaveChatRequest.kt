package chat.com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class JoinOrLeaveChatRequest (
    val chatId: Int,
    val userId: Int
)