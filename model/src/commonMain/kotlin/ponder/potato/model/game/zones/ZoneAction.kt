package ponder.potato.model.game.zones

data class ZoneAction(
    val ability: ZoneAbility,
    val status: String?,
    val cost: Double,
    val currentResource: Double,
    val count: Int?,
    val maxCount: Int?,
    val block: () -> Unit,
) {
    val ratio get() = currentResource / cost
}

