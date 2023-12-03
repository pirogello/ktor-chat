package chat.com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateJwtTokenResponse(
    val isSuccess: Boolean,
    val token: String,
    val username: String
)

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)