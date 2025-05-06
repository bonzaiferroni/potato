package ponder.potato.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ponder.potato.LaunchedGameUpdate
import pondui.ui.controls.Card
import pondui.ui.controls.H5
import pondui.ui.controls.ProgressBar
import pondui.ui.controls.Text
import pondui.ui.nav.Scaffold
import pondui.ui.theme.Pond
import pondui.utils.format

@Composable
fun EntityListScreen(
    viewModel: EntityListModel = viewModel { EntityListModel() }
) {
    val state by viewModel.state.collectAsState()

    LaunchedGameUpdate(viewModel::update)

    Scaffold {
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
                        // Text(entity.name)
                        Column {
                            H5("${entity.type} ${entity.id}")
                            Row {
                                LabelValue("x", entity.x, modifier = Modifier.width(50.dp))
                                LabelValue("y", entity.y)
                            }
                            LabelValue("zone", entity.zoneName)
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
        }
    }
}