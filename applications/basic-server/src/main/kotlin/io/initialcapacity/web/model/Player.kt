package io.initialcapacity.web.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class Player(
        @SerialName("_id") @Contextual val id: ObjectId? = null,  // map Mongo _id
        val name: String,
        val score: Int = 0
)