package ponder.potato.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kabinet.utils.toMetricString
import kotlinx.collections.immutable.ImmutableList
import ponder.potato.model.game.Resource
import ponder.potato.model.game.IntValue
import ponder.potato.model.game.ProgressValue
import ponder.potato.model.game.ResourceStatus
import ponder.potato.model.game.ZoneStatus
import pondui.ui.controls.ProgressBar
import pondui.ui.controls.Text
import pondui.ui.theme.Pond

@Composable
fun StatusBar(
    statuses: ImmutableList<ZoneStatus>
) {
    FlowRow(
        horizontalArrangement = Pond.ruler.rowTight,
        verticalArrangement = Pond.ruler.columnTight,
        itemVerticalAlignment = Alignment.CenterVertically
    ) {
        for (status in statuses) {
            when (status) {
                is ResourceStatus -> {
                    ProgressBar(
                        progress = (status.current / status.max).toFloat(),
                        color = status.resource.toColor(),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("${status.resource}: ${status.current.toMetricString()}")
                            Text(status.max.toMetricString())
                        }
                    }
                }

                is IntValue -> {
                    Text("${status.label}: ${status.value}", modifier = Modifier.weight(1f))
                }

                is ProgressValue -> {
                    Row(
                        horizontalArrangement = Pond.ruler.rowTight,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(status.label)
                        ProgressBar(status.progress, color = Resource.Aether.toColor())
                    }
                }
                // Add more cases as needed
            }
        }
    }

}
