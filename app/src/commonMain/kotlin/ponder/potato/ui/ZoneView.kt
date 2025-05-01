package ponder.potato.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import ponder.potato.LocalGame
import ponder.potato.model.game.zones.Zone
import pondui.ui.controls.Text
import pondui.ui.core.PondApp
import pondui.ui.theme.Pond
import kotlin.reflect.KClass

@Composable
fun <T: Zone> ZoneView(
    zoneClass: KClass<T>,
    modifier: Modifier = Modifier,
    viewModel: ZoneViewModel<T> = viewModel { ZoneViewModel(zoneClass) }
) {
    val state by viewModel.state.collectAsState()
    val gameState by LocalGame.current.state.collectAsState()

    LaunchedEffect(gameState) {
        viewModel.update(gameState)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Pond.ruler.columnTight
    ) {
        Text(state.name)
        Box(
            modifier = modifier.fillMaxWidth()
                .aspectRatio(2f)
                .background(Color.Blue.copy(.1f))
                .clipToBounds()
        ) {
            for (entityId in state.entityIds) {
                EntityView(entityId)
            }
        }
    }
}