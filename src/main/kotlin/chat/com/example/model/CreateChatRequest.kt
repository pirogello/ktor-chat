package chat.com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateChatRequest (
    val name: String,
    val creatorId: Int
)