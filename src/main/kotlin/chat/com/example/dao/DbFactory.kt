package chat.com.example.dao

import chat.com.example.model.table.Chats
import chat.com.example.model.table.Messages
import chat.com.example.model.table.Users
import chat.com.example.model.table.UsersInChats
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun hikari(url: String, user: String, password: String, pool: Int, driver: String): HikariDataSource {
    var config = HikariConfig()
    config.driverClassName = driver
    config.jdbcUrl = url
    config.username = user
    config.password = password
    config.maximumPoolSize = pool
    config.isAutoCommit = false
    config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
    config.validate()
    return HikariDataSource(config)

}

object DbFactory {
    fun init(environment: ApplicationEnvironment) {
        val url = environment.config.property("db.url").getString()
        val user = environment.config.property("db.user").getString()
        val password = environment.config.property("db.pass").getString()
        val pool = environment.config.property("db.pool").getString().toInt()
        val driver = environment.config.property("db.driver").getString()

        Database.connect(hikari(url, user, password, pool, driver))

        transaction {
            SchemaUtils.create(
                Users,
                Chats,
                UsersInChats,
                Messages
            )
        }
    }
}

suspend fun <T> query(
    block: () -> T
): T = withContext(Dispatchers.IO) {
    transaction { block() }
}
