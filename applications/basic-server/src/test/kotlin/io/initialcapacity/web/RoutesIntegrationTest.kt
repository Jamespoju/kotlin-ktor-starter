package io.initialcapacity.web

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RoutesIntegrationTest {

    @Test
    fun root_returns_200() = testApplication {
        application {
            module()
        }
        val res = client.get("/")
        assertEquals(HttpStatusCode.OK, res.status)
    }

    // If you later add /health, uncomment and update the path:
    // @Test
    // fun health_returns_200() = testApplication {
    //     val res = client.get("/health")
    //     assertEquals(HttpStatusCode.OK, res.status)
    // }
}
