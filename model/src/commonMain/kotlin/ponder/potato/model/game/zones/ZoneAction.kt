package ponder.potato.model.game.zones

import ponder.potato.model.game.Resource

data class ZoneAction(
    val action: Actions,
    val status: String?,
    val cost: Double,
    val currentResource: Double,
    val count: Int?,
    val maxCount: Int?,
    val block: () -> Unit,
) {
    val ratio get() = currentResource / cost
}

