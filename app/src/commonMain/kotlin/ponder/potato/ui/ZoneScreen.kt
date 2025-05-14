package ponder.potato.ui

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ponder.potato.LaunchedGameUpdate
import ponder.potato.ZoneRoute
import pondui.ui.controls.Tab
import pondui.ui.controls.Tabs
import pondui.ui.controls.Text
import pondui.ui.nav.LocalPortal
import pondui.ui.nav.TopBarSpacer

@Composable
fun ZoneScreen(
    route: ZoneRoute,
    viewModel: ZoneModel = viewModel { ZoneModel(route.zoneId) }
) {
    val state by viewModel.state.collectAsState()
    val portal = LocalPortal.current

    LaunchedEffect(Unit) {
        viewModel.init()
        portal.setTitle(viewModel.zone.name)
    }

    LaunchedGameUpdate(viewModel::update)

    TopBarSpacer()

    ZoneView(viewModel.zone::class)
    StatusBar(state.statuses)

    Tabs("Actions") {
        Tab("Area") {
            AreaView(viewModel.zone::class)
        }
        Tab("Actions", scrollable = false) {
            ActionsView(state.entityActions, state.zoneActions) { viewModel.refreshState() }
            LazyColumn(modifier = Modifier.height(30.dp)) {
                items(state.messages) {
                    Text(it)
                }
            }
        }
        Tab("Entities") {

        }
    }
}