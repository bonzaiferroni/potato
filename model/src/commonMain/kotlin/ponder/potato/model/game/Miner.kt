package ponder.potato.model.game

interface MinerState: TargetState, ProgressState

class Miner(
    override val entity: StateEntity<MinerState>
): StateComponent<MinerState>() {

    override fun update(delta: Double) {
        if (!entity.hasIntent(Intent.Mine)) return
        val target = entity.readTarget<MinerTargetState>() ?: return
        val isArrived = entity.approach(target.position, delta)
        if (!isArrived) return
        val progress = state.progress ?: 0f

        if (progress < 1f) {
            val additionalProgress = (delta / target.state.hardness).toFloat() // todo: add power
            state.progress = progress + minOf(additionalProgress, 1f - progress)
            return
        }

        val quantity = 10.0 // todo: factor quantity
        val type = target.state.composition.roll()
        val addedQuantity = game.storage.addQuantity(type, quantity)
        state.progress = 0f
        if (addedQuantity < quantity) {
            state.intent = null
            state.targetId = null
            state.progress = null
            if (console.isObserved(entity))
                console.log("${entity.name} is unable to mine more ${type.label}, storage is full.")
        }
    }
}