package ponder.potato.ui

import androidx.compose.runtime.*
import ponder.potato.LocalGameHost
import ponder.potato.model.game.EntityState
import ponder.potato.model.game.Game
import ponder.potato.model.game.StateEntity
import ponder.potato.model.game.castIfState

@Composable
inline fun <reified T: EntityState> InflateEntity(
    entityId: Long,
    content: @Composable (StateEntity<T>, Game) -> Unit
) {
    val gameHost = LocalGameHost.current
    val game = gameHost.game
    val entity = game.entities[entityId]?.castIfState<T>()
    if (entity == null) return
    content(entity, game)
}

@Composable
inline fun <reified T: EntityState> InflateEntityWithDelta(
    entityId: Long,
    content: @Composable (StateEntity<T>, Game, Double) -> Unit
) {
    val gameHost = LocalGameHost.current
    val game = gameHost.game
    val entity = game.entities[entityId]?.castIfState<T>()
    if (entity == null) return
    val state by gameHost.state.collectAsState()
    content(entity, game, state.delta)
}