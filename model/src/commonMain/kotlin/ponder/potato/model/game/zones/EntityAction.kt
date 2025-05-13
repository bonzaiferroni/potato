package ponder.potato.model.game.zones

import kotlinx.datetime.Instant

data class EntityAction(
    val entityId: Long,
    val ability: EntityAbility,
    val label: String = ability.label,
    val lastUsed: Instant? = null,
    val invoke: () -> Unit,
)

enum class EntityAbility(val label: String, val coolDown: Float? = null) {
    Shout("Shout", 5f),
    Mine("Mine"),
}