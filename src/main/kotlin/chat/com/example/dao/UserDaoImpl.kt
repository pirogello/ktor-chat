package chat.com.example.dao

import chat.com.example.model.CreateUserRequest
import chat.com.example.model.User
import chat.com.example.model.table.Users
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.mindrot.jbcrypt.BCrypt

class UserDaoImpl: UserDao {
    private fun rowToUser(row: ResultRow) = User(
        id = row[Users.id],
        username = row[Users.username],
        password = row[Users.password]
    )

    override suspend fun getUsers() = query {
        Users.selectAll().map { rowToUser(it) }
    }

    override suspend fun getUser(id: Int): User? = query {
        Users.select(Users.id eq id)
            .map { rowToUser(it) }
            .singleOrNull()
    }

    override suspend fun getUserByUsername(username: String): User? = query {
        Users.select(Users.username eq username)
            .map { rowToUser(it) }
            .singleOrNull()
    }

    override suspend fun createUser(dto: CreateUserRequest) = query{
        val statement = Users.insert {
            it[username] = dto.username
            it[password] =  BCrypt.hashpw(dto.password, BCrypt.gensalt())
        }
    }
}