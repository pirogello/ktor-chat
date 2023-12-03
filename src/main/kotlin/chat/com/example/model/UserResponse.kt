package chat.com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: Int? = null,
    val username: String? = null

)