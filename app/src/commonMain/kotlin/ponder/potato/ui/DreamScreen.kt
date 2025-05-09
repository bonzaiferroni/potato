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
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringArrayResource
import ponder.potato.LaunchedGameUpdate
import ponder.potato.model.game.Resource
import ponder.potato.model.game.zones.Cave
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
            Card(
                innerPadding = 0.dp,
            ) {
                Row {
                    Image(
                        painter = painterResource(Res.drawable.dream_card_full),
                        contentDescription = "Image of purchase",
                        modifier = Modifier.width(200.dp)
                    )
                    ProvideBookColors {
                        Column(
                            verticalArrangement = Pond.ruler.columnTight,
                            modifier = Modifier.fillMaxWidth()
                                .background(Pond.localColors.surface)
                                .padding(Pond.ruler.innerPadding)
                        ) {
                            H4("The Dream", color = Pond.colors.tertiary)
                            for (text in stringArrayResource(Res.array.dream_description)) {
                                Text(text)
                            }
                        }
                    }
                }
            }
        }
        Tab("Shape Dream", scrollable = false) {

            LazyColumn(
                verticalArrangement = Pond.ruler.columnTight
            ) {
                items(state.actions) { action ->
                    PurchaseBar(action)
                }
                item {
                    BottomBarSpacer()
                }
            }
        }
        Tab("Entities", scrollable = false) {
            EntityListView(viewModel.caveId)
        }
    }
}

