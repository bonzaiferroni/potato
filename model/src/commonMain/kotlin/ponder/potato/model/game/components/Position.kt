package ponder.potato.model.game.components

import kotlinx.serialization.Serializable
import ponder.potato.model.game.Vector
import ponder.potato.model.game.entities.EntityState
import ponder.potato.model.game.entities.StateEntity
import ponder.potato.model.game.zones.GameZone
import kotlin.math.sqrt

interface Position : Vector {
    val zoneId: Int

    fun atPosition(position: Position) = x == position.x && y == position.y && zoneId == position.zoneId
}

class PositionComponent(
    entity: StateEntity<PositionState>,
) : StateComponent<PositionState>(entity) {
    override fun enter(zone: GameZone) {
        super.enter(zone)
        entity.state.position.zoneId = zone.id
    }
}

@Serializable
data class MutablePosition(
    override var zoneId: Int = 0,
    override var x: Float = 0.0f,
    override var y: Float = 0.0f
) : Position {

    fun moveToward(position: Position, delta: Double, speed: Double = 1.0) {
        val potential = delta * speed
        val dx = position.x - x
        val dy = position.y - y
        val distance = sqrt(dx * dx + dy * dy)
        if (distance <= potential || distance == 0f) {
            x = position.x
            y = position.y
            return
        }

        val ratio = potential / distance
        x = (dx * ratio).toFloat()
        y = (dy * ratio).toFloat()
    }
}

interface PositionState: EntityState {
    override val position: MutablePosition
}