package ponder.potato.model.game.zones

import ponder.potato.model.game.components.NameState
import ponder.potato.model.game.entities.Entity
import ponder.potato.model.game.entities.Sprite
import ponder.potato.model.game.entities.StateEntity

class NamingWay(
    private val sprites: List<String> = emptyList()
) {
    fun getName(entity: StateEntity<NameState>) = when (entity) {
        is Sprite -> sprites.takeIf { it.isNotEmpty() }?.random()
        else -> null
    }
}