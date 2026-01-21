package io.initialcapacity.web

import java.util.concurrent.atomic.AtomicLongArray
import kotlin.math.max

/** Lightweight rolling 60s request-rate meter */
object RequestRateMeter {
    private const val WINDOW_SECONDS = 60
    private val buckets = AtomicLongArray(WINDOW_SECONDS)
    @Volatile private var lastEpochSec: Long = 0

    private fun currentSecond(): Long = System.currentTimeMillis() / 1000

    fun mark() {
        val now = currentSecond()
        val idx = (now % WINDOW_SECONDS).toInt()

        // If we’ve moved to a new second, clear the bucket for this second.
        val last = lastEpochSec
        if (now != last) {
            // Clear all skipped seconds’ buckets (handles long gaps safely)
            var s = max(last + 1, now - WINDOW_SECONDS + 1)
            while (s <= now) {
                buckets.set((s % WINDOW_SECONDS).toInt(), 0)
                s++
            }
            lastEpochSec = now
        }
        buckets.incrementAndGet(idx)
    }

    fun rps(): Double {
        val now = currentSecond()
        var total = 0L
        for (i in 0 until WINDOW_SECONDS) {
            val sec = (now - (WINDOW_SECONDS - 1 - i))
            if (sec >= 0) total += buckets.get((sec % WINDOW_SECONDS).toInt())
        }
        return total.toDouble() / WINDOW_SECONDS
    }
}