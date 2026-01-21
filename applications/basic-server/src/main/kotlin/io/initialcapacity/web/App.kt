package io.initialcapacity.web

import freemarker.cache.ClassTemplateLoader
import io.initialcapacity.web.model.Player
import io.initialcapacity.web.repo.PlayerRepo
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.freemarker.*
import io.ktor.server.http.content.staticResources
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory
import java.util.*
import io.ktor.util.pipeline.PipelineContext

private val logger = LoggerFactory.getLogger(object {}.javaClass.enclosingClass)

fun Application.module() {
    logger.info("starting the app")

    // JSON for /players
    install(ContentNegotiation) { json() }

    // Templates for homepage
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    // Mongo config (reads from application.conf if present, else defaults)
    val uri = try { environment.config.property("mongo.uri").getString() } catch (_: Exception) { "mongodb://localhost:27017" }
    val dbName = try { environment.config.property("mongo.db").getString() } catch (_: Exception) { "game_paulse" }
    Mongo.init(uri, dbName)

    // Routing block
    install(Routing) {
        get("/") {
            call.respond(FreeMarkerContent("index.ftl", mapOf("headers" to headers())))
        }
        staticResources("/static/styles", "static/styles")
        staticResources("/static/images", "static/images")

        // health
        get("/ping") { call.respondText("pong") }

        // players API
        route("/players") {
            post {
                val body = call.receive<Player>()
                val created = PlayerRepo.create(body)
                call.respond(HttpStatusCode.Created, created)
            }
            get {
                call.respond(PlayerRepo.list())
            }
            get("/top") {
                call.respond(PlayerRepo.top(20))
            }
        }

        get("/leaderboard") {
            call.respond(
                mapOf(
                    "games" to listOf(
                        mapOf(
                            "id" to "game-1",
                            "name" to "Example Game",
                            "trendingScore" to 0.85
                        )
                    )
                )
            )
        }

        get("/games") {
            val games = io.initialcapacity.web.repo.GameRepo.list()
            call.respond(games)
        }

        get("/health") {
            call.respond(HttpStatusCode.OK, mapOf("status" to "healthy"))
        }
        get("/metrics") {
            val rps = String.format("%.4f", RequestRateMeter.rps())
            call.respond(HttpStatusCode.OK, mapOf("requests_per_second" to rps))
        }
    }
}

private fun PipelineContext<Unit, ApplicationCall>.headers(): MutableMap<String, String> {
    val headers = mutableMapOf<String, String>()
    call.request.headers.entries().forEach { entry ->
        headers[entry.key] = entry.value.joinToString()
    }
    return headers
}

fun main() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    val port = System.getenv("PORT")?.toInt() ?: 8080
    embeddedServer(Netty, port = port, host = "0.0.0.0", module = { module() }).start(wait = true)
}