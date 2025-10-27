package io.initialcapacity.web

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

// GamePulse: simple composite "trending score" for unit testing.
// Kept in test scope so we don't change production code.
private fun trendingScore(steamPlayers: Int, twitchViewers: Int): Double {
    require(steamPlayers >= 0 && twitchViewers >= 0)
    return steamPlayers * 0.6 + twitchViewers * 0.4
}

class ScoringTest {
    @Test
    fun score_is_weighted_sum() {
        val score = trendingScore(1000, 500)
        assertEquals(1000 * 0.6 + 500 * 0.4, score, 0.0001)
    }

    @Test
    fun rejects_negative_inputs() {
        assertFailsWith<IllegalArgumentException> { trendingScore(-1, 0) }
    }
}
