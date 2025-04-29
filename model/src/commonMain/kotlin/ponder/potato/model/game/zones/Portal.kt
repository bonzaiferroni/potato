package ponder.potato.model.game.zones

import ponder.potato.model.game.components.Position

interface Portal {
    val zone: Zone
}

class ZonePortal(
    override val zone: GameZone,
    override val x: Float,
    override val y: Float,
): Portal, Position {
    override val zoneId get() = zone.id
}