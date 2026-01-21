package io.initialcapacity.web

import io.initialcapacity.web.model.Game
import io.initialcapacity.web.repo.GameRepo
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * Standalone collector for assignment:
 * - Fetches games from FreeToGame API (no auth)
 * - Upsert into MongoDB
 *
 * Run:
 *   ./gradlew :applications:basic-server:runCollectorGames
 */
suspend fun fetchGames(limit: Int = 10): List<Game> {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }
    client.use {
        val url = "https://www.freetogame.com/api/games"
        val all: List<Game> = it.get(url).body()
        return all.take(limit)
    }
}

suspend fun runCollectorGames() {
    // Load config from application.conf if present; else use defaults
    val uri = System.getProperty("mongo.uri") ?: "mongodb://localhost:27017"
    val dbName = System.getProperty("mongo.db") ?: "game_paulse"

    Mongo.init(uri, dbName)
    GameRepo.ensureIndexes()

    val batch = fetchGames(limit = 10)
    for (g in batch) {
        GameRepo.upsert(g)
    }
    println("Upserted ${batch.size} games into Mongo.")
}

suspend fun main() {
    try {
        runCollectorGames()
    } catch (e: Exception) {
        System.err.println("Collector failed: ${e.message}")
        throw e
    }
}