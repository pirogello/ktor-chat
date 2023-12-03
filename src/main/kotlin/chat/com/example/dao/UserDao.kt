package chat.com.example.dao

import chat.com.example.model.CreateUserRequest
import chat.com.example.model.User

interface UserDao {
    suspend fun getUsers(): List<User>
    suspend fun getUser(id: Int): User?
    suspend fun getUserByUsername(username: String): User?
    suspend fun createUser(dto: CreateUserRequest)
}