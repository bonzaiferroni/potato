package ponder.potato.model.game.zones

import ponder.potato.model.game.Position

interface Portal {
    val zone: Zone
}

class ZonePortal<S>(
    val zone: StateZone<S>,
    override val x: Float,
    override val y: Float,
): Portal, Position