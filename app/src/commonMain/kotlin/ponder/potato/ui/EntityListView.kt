package ponder.potato.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ponder.potato.LaunchedGameUpdate
import ponder.potato.model.game.components.NameState
import ponder.potato.model.game.entities.StateEntity
import pondui.ui.controls.Card
import pondui.ui.controls.H4
import pondui.ui.controls.H5
import pondui.ui.controls.ProgressBar
import pondui.ui.controls.Text
import pondui.ui.nav.BottomBarSpacer
import pondui.ui.nav.TopBarSpacer
import pondui.ui.theme.Pond

@Composable
fun EntityListScreen() {
    TopBarSpacer()
    EntityListView()
}

@Composable
fun EntityListView(
    caveId: Int? = null,
    viewModel: EntityListModel = viewModel { EntityListModel(caveId) }
) {
    val state by viewModel.state.collectAsState()

    LaunchedGameUpdate(viewModel::update)

    LazyColumn(
        verticalArrangement = Pond.ruler.columnTight
    ) {
        items(state.entities) { entity ->
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        val name = entity.name
                        if (name != null) {
                            H5("$name (${entity.type})")
                        } else {
                            H5("${entity.type} ${entity.id}")
                        }
                        Row {
                            LabelValue("x", entity.x, modifier = Modifier.width(50.dp))
                            LabelValue("y", entity.y)
                        }
                        LabelValue("zone", entity.zoneName)
                        entity.status?.let {
                            LabelValue("status", it)
                        }
                    }
                    val spirit = entity.spirit;
                    val maxSpirit = entity.maxSpirit
                    if (spirit != null && maxSpirit != null) {
                        ProgressBar(spirit / maxSpirit.toFloat()) {
                            Text("Spirit: $spirit / $maxSpirit")
                        }
                    }
                }
            }
        }

        item {
            BottomBarSpacer()
        }
    }
}