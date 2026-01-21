package io.initialcapacity.web.model

import kotlinx.serialization.Serializable

@Serializable
data class Game(
    val id: Int,
    val title: String,
    val genre: String ? = null,
    val platform: String? = null,
    val shortDescription: String? = null
)