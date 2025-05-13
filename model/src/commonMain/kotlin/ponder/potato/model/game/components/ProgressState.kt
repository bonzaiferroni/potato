package ponder.potato.model.game.components

import ponder.potato.model.game.entities.EntityState

interface ProgressState: EntityState {
    var progress: Float
}