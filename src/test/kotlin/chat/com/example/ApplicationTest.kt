package chat.com.example

import chat.com.example.plugins.configureRoutes
import chat.com.example.plugins.configureSerialization
import io.ktor.server.testing.*
import kotlin.test.Test

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureRoutes()
            configureSerialization()
        }
//        client.get("/").apply {
//            assertEquals(HttpStatusCode.OK, status)
//            assertEquals("Hello World!", bodyAsText())
//        }
    }
}
