package ponder.potato.model.game

import kotlin.math.pow

fun factorValue(initialProgress: Int, level: Int, growthRate: Double) =
    initialProgress * growthRate.pow(level - 1)
