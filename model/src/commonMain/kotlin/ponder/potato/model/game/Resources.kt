package ponder.potato.model.game

interface Resources {
    val aether: Double
}

data class GameResources(
    override var aether: Double = 1000.0
): Resources