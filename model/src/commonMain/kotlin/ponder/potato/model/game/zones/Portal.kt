package ponder.potato.model.game.zones

import ponder.potato.model.game.Position

//interface Portal: Position {
//    val zone: Zone
//    override val x: Float
//    override val y: Float
//    override val zoneId get() = zone.id
//}

class Portal(
    val zone: GameZone,
    override val x: Float,
    override val y: Float,
): Position {
    override val zoneId get() = zone.id
}