package ponder.potato.model.game

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

data class ResourceReward(
    val amount: Double
): Effect()

class RaiseGhost: Effect()

data class LevelUp(
    val level: Int
): Effect()

data class ExperienceUp(
    val experience: Double
): Effect()

data class Exec(
    val execution: Execution
): Effect()