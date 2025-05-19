package ponder.potato.ui

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.collections.immutable.persistentListOf
import ponder.potato.LaunchedGameUpdate
import ponder.potato.model.game.Cave
import ponder.potato.model.game.Dream
import pondui.ui.controls.*
import pondui.ui.nav.TopBarSpacer

@Composable
fun DreamScreen(
    viewModel: DreamModel = viewModel { DreamModel() }
) {
    val state by viewModel.state.collectAsState()

    LaunchedGameUpdate(viewModel::update)

    TopBarSpacer()

    ZoneView(
        zoneClass = Cave::class,
        highlightedId = null,
        onHoverChange = { id, isHovered -> },
        onClick = { }
    )
    StatusBar(state.statuses)

    Tabs {
        Tab("Area") {
            AreaView(Dream::class)
        }
        Tab("Shape Dream", scrollable = false) {
            ActionsView(persistentListOf(), state.actions) { viewModel.refreshState() }
        }
        Tab("Entities", scrollable = false) {
            EntityListView(viewModel.caveId)
        }
    }
}

