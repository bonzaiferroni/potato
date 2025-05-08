package ponder.potato.model.game.components

import ponder.potato.model.game.entities.EntityState

interface VisitorState: EntityState {
    var visitorId: Long?
}