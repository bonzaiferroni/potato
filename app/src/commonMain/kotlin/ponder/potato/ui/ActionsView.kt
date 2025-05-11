package ponder.potato.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import kotlinx.collections.immutable.ImmutableList
import ponder.potato.model.game.zones.ZoneAction
import pondui.ui.nav.BottomBarSpacer
import pondui.ui.theme.Pond

@Composable
fun ActionsView(
    actions: ImmutableList<ZoneAction>,
    onAction: () -> Unit,
) {
    LazyColumn(
        verticalArrangement = Pond.ruler.columnTight
    ) {
        items(actions) { action ->
            PurchaseBar(action, onAction)
        }
        item {
            BottomBarSpacer()
        }
    }
}