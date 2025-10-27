package io.initialcapacity.web.repo

import com.mongodb.client.model.Indexes
import com.mongodb.client.model.ReplaceOptions
import io.initialcapacity.web.Mongo
import io.initialcapacity.web.model.Game
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq

object GameRepo {
    private val col: CoroutineCollection<Game>
        get() = Mongo.db.getCollection<Game>("games")

    suspend fun ensureIndexes() {
        col.createIndex(Indexes.ascending("id"))
        col.createIndex(Indexes.ascending("title"))
    }

    suspend fun upsert(game: Game) {
        col.replaceOne(Game::id eq game.id, game, ReplaceOptions().upsert(true))
    }

    suspend fun list(limit: Int = 50): List<Game> = col.find().limit(limit).toList()
}