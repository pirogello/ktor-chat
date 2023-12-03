package chat.com.example.service

import chat.com.example.model.CreateUserRequest
import chat.com.example.model.UserResponse

interface UserService {
    suspend fun getUsers(): List<UserResponse?>
    suspend fun getUserById(id: Int): UserResponse?
    suspend fun createUser(dto: CreateUserRequest)
}