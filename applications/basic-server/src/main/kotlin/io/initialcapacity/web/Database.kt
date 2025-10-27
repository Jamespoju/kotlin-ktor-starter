package io.initialcapacity.web

import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.coroutine.CoroutineDatabase

object Mongo {
    lateinit var db: CoroutineDatabase
        private set

    fun init(uri: String, dbName: String) {
        db = KMongo.createClient(uri).coroutine.getDatabase(dbName)
    }
}