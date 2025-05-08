package ponder.potato.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import ponder.potato.GameService
import ponder.potato.ZoneRoute
import ponder.potato.model.game.zones.Cave

@Composable
fun CaveScreen() {
    val caveId = remember { GameService().game.zones.first { it is Cave }.id }
    ZoneScreen(ZoneRoute(caveId))
}