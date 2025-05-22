package ponder.potato.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pondui.ui.controls.Text

@Composable
fun InstructionItemView(
    item: InstructionItem,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        item.scopeName?.let {
            Text("$it.")
        }
        Text(item.name)
        item.parameterName?.let {
            Text("($it)")
        }
    }
}