package chat.com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Chat (
    val id: Int,
    val name: String,
    val adminId: Int
)