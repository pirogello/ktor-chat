package chat.com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatWithParticipantsResponse (
    val id: Int,
    val name: String,
    val adminId: Int,
    val participants: List<UserResponse>
)