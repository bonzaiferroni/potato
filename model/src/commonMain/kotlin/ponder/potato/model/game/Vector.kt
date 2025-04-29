package ponder.potato.model.game

interface Vector {
    val x: Float
    val y: Float
}

data class LocalPoint(
    override val x: Float,
    override val y: Float,
): Vector