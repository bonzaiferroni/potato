package ponder.potato.model.game.components

import ponder.potato.model.game.entities.EntityState
import ponder.potato.model.game.entities.StateEntity
import ponder.potato.model.game.findNearest
import ponder.potato.model.game.read

interface TargetState : EntityState {
    var targetId: Long?
}

var StateEntity<TargetState>.target
    get() = state.targetId?.let { game.entities[it] }
    set(value) {
        state.targetId = value?.id
    }

inline fun <reified S : EntityState> StateEntity<TargetState>.findTarget(
    range: Float = Float.MAX_VALUE,
    isTarget: (StateEntity<S>) -> Boolean,
) = game.entities.findNearest(position, range, isTarget)?.also { state.targetId = it.id }

inline fun <reified S : EntityState> StateEntity<TargetState>.readOrFindTarget(
    range: Float = Float.MAX_VALUE,
    isTarget: (StateEntity<S>) -> Boolean,
) = readOrClearTarget() ?: findTarget(range, isTarget)

inline fun <reified S : EntityState> StateEntity<TargetState>.readOrClearTarget() = state.targetId?.let {
    val entity = game.entities.read<StateEntity<S>>(it)
    if (entity == null) {
        state.targetId = null
    }
    entity
}