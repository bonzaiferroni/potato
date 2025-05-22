package ponder.potato.model.game

interface TargetState : EntityState {
    var targetId: Long?
}

var StateEntity<TargetState>.target
    get() = state.targetId?.let { game.entities[it] }
    set(value) {
        state.targetId = value?.id
    }

val Entity.target get() = (state as? TargetState)?.targetId?.let { game.entities[it] }

inline fun <reified S : EntityState> StateEntity<TargetState>.findTarget(
    range: Float = Float.MAX_VALUE,
    isTarget: (StateEntity<S>) -> Boolean,
) = game.entities.findNearest(position, range, isTarget)?.also { state.targetId = it.id }

inline fun <reified S : EntityState> StateEntity<TargetState>.readOrFindTarget(
    range: Float = Float.MAX_VALUE,
    isTarget: (StateEntity<S>) -> Boolean,
) = readOrClearTarget() ?: findTarget(range, isTarget)

inline fun <reified S : EntityState> StateEntity<TargetState>.readOrClearTarget() = state.targetId?.let {
    val entity = game.entities.readEntity<StateEntity<S>>(it)
    if (entity == null) {
        state.targetId = null
    }
    entity
}