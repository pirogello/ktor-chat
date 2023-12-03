package chat.com.example.service

import chat.com.example.model.CreateJwtTokenResponse

interface AuthService {
    suspend fun generateToken(username: String, password: String): CreateJwtTokenResponse
}