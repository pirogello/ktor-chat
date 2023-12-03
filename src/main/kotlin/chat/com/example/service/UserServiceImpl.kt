package chat.com.example.service

import chat.com.example.dao.UserDao
import chat.com.example.model.CreateUserRequest
import chat.com.example.model.User
import chat.com.example.model.UserResponse


class UserServiceImpl (
    private val userDao: UserDao
): UserService {
    private fun mapUserToUserResponse(user: User?): UserResponse? {
        if(user == null) return null
        return UserResponse(
            id = user.id,
            username = user.username
        )
    }
    override suspend fun getUsers(): List<UserResponse?> {
        return userDao.getUsers().map { mapUserToUserResponse(it) }
    }

    override suspend fun getUserById(id: Int): UserResponse? {
        return mapUserToUserResponse(userDao.getUser(id))
    }

    override suspend fun createUser(dto: CreateUserRequest) {
        try {
            userDao.createUser(dto)
        } catch (e: Exception){
           throw e
        }
    }
}