package ponder.potato.model.game

import ponder.potato.model.game.entities.Entity

sealed class Effect {
}

data class Opposition(
    val target: Entity,
    val power: Int,
): Effect()

data class Despirit(
    val spirit: Int
): Effect()

data class Inspirit(
    val spirit: Int
): Effect()

data class AetherReward(
    val amount: Double
): Effect()

class RaiseGhost: Effect()

data class LevelUp(
    val level: Int
): Effect()

data class ExperienceUp(
    val experience: Double
): Effect()