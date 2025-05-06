package ponder.potato.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import kabinet.utils.toMetricString
import pondui.ui.controls.Button
import pondui.ui.controls.Card
import pondui.ui.controls.H4
import pondui.ui.controls.Label
import pondui.ui.controls.ProgressBar
import pondui.ui.controls.ProgressBarButton
import pondui.ui.controls.Text
import pondui.ui.controls.actionable
import pondui.ui.theme.Pond


@Composable
fun PurchaseBar(
    label: String,
    cost: Double,
    ratio: Double?,
    buttonLabel: String = "Dream",
    purchase: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .heightIn(min = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            H4(label)
            val canPurchase = ratio?.let { it >= 1f } ?: false
            val labelText = when {
                canPurchase -> buttonLabel
                ratio == null -> "Full"
                else -> cost.toMetricString()
            }
            ProgressBarButton(
                ratio = ratio?.toFloat() ?: 1f,
                labelText = labelText,
                isEnabled = canPurchase,
                onClick = purchase
            )
        }
        content()
    }
}