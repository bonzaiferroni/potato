package ponder.potato.model.game.components

import ponder.potato.model.game.entities.EntityState
import ponder.potato.model.game.entities.StateEntity

interface NameState: EntityState {
    var name: String?
}

class Namer(
    override val entity: StateEntity<NameState>
): StateComponent<NameState>() {
    override fun init() {
        super.init()
        if (state.name != null) return

        var name: String?
        var count = 0
        do {
            name = game.namingWay.getName(entity)
        } while (count++ < 10 && game.entities.values.any { (it.state as? NameState)?.name == name })
        entity.state.name = name
        entity.state.log = "A spirit takes a shape and a name: $name"
    }

    override fun recycle() {
        super.recycle()
        state.name = null
    }
}