package ponder.potato.model.game

import kotlin.math.ceil
import kotlin.math.pow

fun factorValue(initialProgress: Int, level: Int, growthRate: Double): Double {
    val raw = initialProgress * growthRate.pow(level - 1)
    return if (raw > 50) ceil(raw / 50) * 50 else raw
}

