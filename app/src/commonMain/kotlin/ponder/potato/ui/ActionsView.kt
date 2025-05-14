package ponder.potato.ui

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import kotlinx.collections.immutable.ImmutableList
import ponder.potato.model.game.EntityAction
import ponder.potato.model.game.ZoneAction
import pondui.ui.controls.Button
import pondui.ui.nav.BottomBarSpacer
import pondui.ui.theme.Pond

@Composable
fun ActionsView(
    entityActions: ImmutableList<EntityAction>,
    zoneActions: ImmutableList<ZoneAction>,
    onAction: () -> Unit,
) {
    LazyColumn(
        verticalArrangement = Pond.ruler.columnTight
    ) {
        if (entityActions.isNotEmpty()) {
            item {
                FlowRow(
                    verticalArrangement = Pond.ruler.columnTight,
                    horizontalArrangement = Pond.ruler.rowTight
                ) {
                    for (action in entityActions) {
                        Button(action.label, onClick = action.invoke)
                    }
                }
            }
        }
        items(zoneActions) { action ->
            PurchaseBar(action, onAction)
        }
        item {
            BottomBarSpacer()
        }
    }
}