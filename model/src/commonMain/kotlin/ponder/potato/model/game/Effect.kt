package ponder.potato.model.game

import ponder.potato.model.game.entities.Entity

sealed class Effect {
}

data class OpposeEffect(
    val target: Entity,
    val power: Int,
) : Effect()

data class Despirit(
    val spirit: Int
) : Effect()

data class AetherReward(
    val amount: Double
) : Effect()

class RaiseGhost : Effect()