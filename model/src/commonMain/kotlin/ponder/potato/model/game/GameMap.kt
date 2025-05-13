package ponder.potato.model.game

class GameMap(
    val dream: Dream,
    val cave: Cave,
)

inline fun <reified T: Zone> Game.readZone() = zones.first { it is T } as T