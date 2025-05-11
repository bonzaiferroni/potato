package ponder.potato.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kabinet.utils.toMetricString
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringArrayResource
import ponder.potato.LaunchedGameUpdate
import ponder.potato.model.game.Resource
import ponder.potato.model.game.zones.Cave
import ponder.potato.model.game.zones.Dream
import ponder.potato.model.game.zones.IntValue
import ponder.potato.model.game.zones.ProgressValue
import ponder.potato.model.game.zones.ResourceStatus
import pondui.ui.controls.*
import pondui.ui.nav.BottomBarSpacer
import pondui.ui.nav.TopBarSpacer
import pondui.ui.theme.Pond
import pondui.ui.theme.ProvideBookColors
import potato.app.generated.resources.Res
import potato.app.generated.resources.dream_card_full
import potato.app.generated.resources.dream_description

@Composable
fun DreamScreen(
    viewModel: DreamModel = viewModel { DreamModel() }
) {
    val state by viewModel.state.collectAsState()

    LaunchedGameUpdate(viewModel::update)

    TopBarSpacer()

    ZoneView(Cave::class)
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

