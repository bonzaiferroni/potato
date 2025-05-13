package ponder.potato.model.game

interface DreamerState: EntityState {
    val aetherReward: Double

    fun getReward(dreamLevel: Int) = factorValue(aetherReward.toInt(), dreamLevel, 1.5)
}

class Dreamer(
    override val entity: StateEntity<DreamerState>
) : StateComponent<DreamerState>() {
}