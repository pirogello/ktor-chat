package chat.com.example.service

import chat.com.example.dao.UserDao
import chat.com.example.model.CreateJwtTokenResponse
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import org.mindrot.jbcrypt.BCrypt
import java.time.Instant

class JWTAuthServiceImpl(
    private val userDao: UserDao,
    environment: ApplicationEnvironment
) : AuthService {

    private val secret = environment.config.property("jwt.secret").getString()
    private val issuer = environment.config.property("jwt.issuer").getString()
    private val audience = environment.config.property("jwt.audience").getString()
    private val expired = environment.config.property("jwt.expired").getString().toInt()

    override suspend fun generateToken(username: String, password: String): CreateJwtTokenResponse {
        val user = userDao.getUserByUsername(username) ?: throw Exception("User with username $username not found")
        if(!BCrypt.checkpw(password, user.password)) throw Exception("Invalid username or password")

        val generatedToken = JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("userName", username)
            .withExpiresAt(Instant.ofEpochMilli(System.currentTimeMillis() + expired))
            .sign(Algorithm.HMAC256(secret))

        return  CreateJwtTokenResponse(isSuccess = true, token = generatedToken, username = username)
    }

}