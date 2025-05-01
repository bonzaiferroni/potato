package ponder.potato.model.game.zones

import ponder.potato.model.game.Position
import ponder.potato.model.game.entities.Entity
import ponder.potato.model.game.entities.StateEntity

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
    val remoteX: Float,
    val remoteY: Float,
): Position {
    override val zoneId get() = zone.id

    fun transport(entity: StateEntity<*>) {
        entity.enter(zone)
        entity.state.position.x = remoteX
        entity.state.position.y = remoteY
    }
}