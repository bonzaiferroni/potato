package ponder.potato.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import kabinet.utils.toMetricString
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
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
    cost: Double? = null,
    resourceName: String? = null,
    resourceColor: Color = Pond.localColors.content,
    ratio: Double? = null,
    currentCount: Int? = null,
    maxCount: Int? = null,
    resource: DrawableResource? = null,
    buttonLabel: String = "Dream",
    purchase: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Card(
        shape = Pond.ruler.roundedEnd,
        innerPadding = 0.dp, modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .height(IntrinsicSize.Max),
        ) {
            resource?.let {
                Image(
                    painter = painterResource(resource),
                    contentDescription = "Image of purchase",
                    modifier = Modifier.weight(1f)
                )
            }
            Column(
                verticalArrangement = Pond.ruler.columnTight,
                modifier = Modifier.weight(2f)
                    .padding(Pond.ruler.innerSpacing)
            ) {
                Column(
                    verticalArrangement = Pond.ruler.columnTight,
                    modifier = Modifier.weight(1f)
                ) {
                    H4(label, color = Pond.colors.tertiary)
                    content()
                }
                FlowRow(
                    itemVerticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalArrangement = Pond.ruler.columnTight,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (currentCount != null) {
                        if (maxCount != null) {
                            ValueOfMax(currentCount, maxCount)
                        } else {
                            Text(currentCount.toString())
                        }
                    }
                    if (cost != null && resourceName != null) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(horizontal = Pond.ruler.innerSpacing),
                        ) {
                            Text(cost.toMetricString(), color = resourceColor)
                            Text(" $resourceName", color = Pond.localColors.contentDim)
                        }
                    }
                    val canPurchase = ratio?.let { it >= 1f } ?: false
                    val labelText = when {
                        canPurchase -> buttonLabel
                        ratio == null -> "Full"
                        else -> cost?.toMetricString() ?: "Max"
                    }
                    ProgressBarButton(
                        ratio = ratio?.toFloat() ?: 1f,
                        labelText = labelText,
                        isEnabled = canPurchase,
                        onClick = purchase
                    )
                }
            }
        }
    }
}