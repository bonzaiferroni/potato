package ponder.potato.model.game

import kotlin.math.pow

fun factorValue(initialProgress: Double, level: Int, growthRate: Double) =
    initialProgress * growthRate.pow(level - 1)
