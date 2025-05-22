package ponder.potato.ui

import androidx.compose.runtime.*
import ponder.potato.model.game.EntityState
import pondui.ui.controls.H1

@Composable
fun EntityPanel(
    entityId: Long,
) {
    InflateEntity<EntityState>(entityId) { entity, game ->
        H1(entity.name)
    }

    ProgramPanel(entityId)
}