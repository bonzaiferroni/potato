package ponder.potato.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import ponder.potato.LaunchedGameUpdate
import ponder.potato.ZoneRoute
import ponder.potato.model.game.zones.Cave
import pondui.ui.controls.Tab
import pondui.ui.controls.Tabs
import pondui.ui.nav.TopBarSpacer
import pondui.ui.theme.Pond

@Composable
fun ZoneScreen(
    route: ZoneRoute,
    viewModel: ZoneModel = viewModel { ZoneModel(route.zoneId) }
) {
    val state by viewModel.state.collectAsState()
    LaunchedGameUpdate(viewModel::update)

    TopBarSpacer()

    ZoneView(viewModel.zone::class)
    StatusBar(state.statuses)

    Tabs {
        Tab("Area") {
            AreaView(viewModel.zone::class)
        }
        Tab("Actions", scrollable = false) {
            ActionsView(state.actions) { viewModel.refreshState() }
        }
        Tab("Entities") {

        }
    }
}