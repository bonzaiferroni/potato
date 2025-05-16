package ponder.potato.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import pondui.ui.controls.Text

@Composable
fun InstructionItemView(
    item: InstructionItem
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
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