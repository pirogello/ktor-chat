package chat.com.example.model

import chat.com.example.util.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Message(
    val id: Int,
    val chatId: Int,
    val senderId: Int,
    val content: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime
)