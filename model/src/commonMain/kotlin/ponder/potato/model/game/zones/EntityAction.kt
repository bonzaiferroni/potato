package ponder.potato.model.game.zones

import kotlinx.datetime.Instant

data class EntityAction(
    val entityId: Long,
    val ability: EntityAbility,
    val lastUsed: Instant?,
    val invoke: () -> Unit,
)

enum class EntityAbility(val label: String, val coolDown: Float) {
    Shout("Shout", 5f)
}