package chat.com.example

import chat.com.example.plugins.configureRoutes
import chat.com.example.plugins.configureSerialization
import io.ktor.server.testing.*
import junit.framework.TestCase.assertEquals
import kotlin.test.Test

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            // modules
        }

        assertEquals("Hello World!", "Hello World!")
    }
}
