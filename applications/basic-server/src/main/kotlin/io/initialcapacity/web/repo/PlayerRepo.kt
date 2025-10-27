package io.initialcapacity.web.repo

import com.mongodb.client.model.Indexes
import io.initialcapacity.web.Mongo
import io.initialcapacity.web.model.Player
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.descending

object PlayerRepo {
    private val col: CoroutineCollection<Player>
        get() = Mongo.db.getCollection()

    suspend fun ensureIndexes() {
        col.createIndex(Indexes.descending("score"))
        col.createIndex(Indexes.ascending("name"))
    }

    suspend fun create(p: Player): Player {
        val doc = p.copy(id = null)
        col.insertOne(doc)
        return doc
    }

    suspend fun list(): List<Player> =
            col.find().toList()

    suspend fun top(n: Int = 20): List<Player> =
            col.find().sort(descending(Player::score)).limit(n).toList()
}