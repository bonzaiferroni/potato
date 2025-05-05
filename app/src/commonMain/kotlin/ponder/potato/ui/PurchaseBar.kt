package ponder.potato.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import kabinet.utils.toMetricString
import pondui.ui.controls.Button
import pondui.ui.controls.Card
import pondui.ui.controls.Label
import pondui.ui.controls.ProgressBar
import pondui.ui.controls.Text


@Composable
fun PurchaseBar(
    label: String,
    cost: Double,
    ratio: Double,
    buttonLabel: String = "Dream",
    purchase: () -> Unit,
    content: @Composable () -> Unit,
) {
    val canPurchase = ratio >= 1f
    Card {
        Row(
            modifier = Modifier.fillMaxWidth()
                .heightIn(min = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("$label: ${cost.toMetricString()}")
            if (canPurchase) {
                Button(buttonLabel, isEnabled = canPurchase, onClick = purchase)
            } else {
                ProgressBar(ratio.toFloat()) {
                    Text(cost.toMetricString())
                }
            }
        }
        content()
    }
}