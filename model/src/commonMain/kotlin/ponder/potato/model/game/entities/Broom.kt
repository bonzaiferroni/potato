package ponder.potato.model.game.entities

import kotlinx.serialization.Serializable
import ponder.potato.model.game.components.Mover
import ponder.potato.model.game.components.MoverComponent
import ponder.potato.model.game.components.MoverState
import ponder.potato.model.game.components.PositionComponent
import ponder.potato.model.game.components.PositionState
import ponder.potato.model.game.components.VitalityState
import ponder.potato.model.game.factorValue
import ponder.potato.model.game.zones.StateZone

interface Broom: Entity, Mover {
    override val state: BroomState
}

class BroomEntity(
    zone: StateZone<*>,
    state: BroomState = BroomState()
) : StateEntity<BroomState>(zone, state), Broom {
    init {
        add(PositionComponent(this, state.position))
        add(MoverComponent(this, state.target, state.position))
    }
}

@Serializable
data class BroomState(
    override val level: Int = 1,
    override val health: Int = 100,
    override val position: PositionState = PositionState(),
    override val target: PositionState = PositionState()
): EntityState, VitalityState, ProgressState, MoverState {
    override val maxHealth get() = factorValue(100, level, 1.2).toInt()
}