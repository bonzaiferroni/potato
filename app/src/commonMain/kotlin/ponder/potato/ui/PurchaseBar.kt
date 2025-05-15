package ponder.potato.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kabinet.utils.toMetricString
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import ponder.potato.model.game.Resource
import ponder.potato.model.game.ZoneAbility
import ponder.potato.model.game.ZoneAction
import pondui.ui.controls.Card
import pondui.ui.controls.H4
import pondui.ui.controls.ProgressBarButton
import pondui.ui.controls.Text
import pondui.ui.theme.Pond
import potato.app.generated.resources.Res
import potato.app.generated.resources.bardfox_card_full
import potato.app.generated.resources.potato_card_full
import potato.app.generated.resources.shroom_card_full
import potato.app.generated.resources.sprite_card_full


@Composable
fun PurchaseBar(
    label: String,
    cost: Double? = null,
    resource: Resource? = null,
    ratio: Double? = null,
    currentCount: Int? = null,
    maxCount: Int? = null,
    drawable: DrawableResource? = null,
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
            drawable?.let {
                Image(
                    painter = painterResource(drawable),
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
                    if (cost != null && resource != null) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(horizontal = Pond.ruler.innerSpacing),
                        ) {
                            Text(cost.toMetricString(), color = resource.toColor())
                            Text(" ${resource.label}", color = Pond.localColors.contentDim)
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

@Composable
fun PurchaseBar(
    action: ZoneAction,
    onAction: () -> Unit,
) = PurchaseBar(
    label = action.ability.label,
    cost = action.cost,
    resource = action.ability.resource,
    ratio = action.ratio,
    currentCount = action.count,
    maxCount = action.maxCount,
    drawable = action.ability.toResource(),
    buttonLabel = action.ability.verb,
    purchase = {
        action.block()
        onAction()
    },
) {
    Text(action.ability.description)
    action.status?.let {
        Text(it)
    }
}

fun Resource.toColor() = when (this) {
    Resource.Aether -> Color(0xffb13c91)
    Resource.Dirt -> Color(0xff6e4e3c)
    Resource.Gold -> Color(0xffb69511)
}

fun ZoneAbility.toResource() = when (this) {
    ZoneAbility.DreamSprite -> Res.drawable.sprite_card_full
    ZoneAbility.DreamShroom -> Res.drawable.shroom_card_full
    ZoneAbility.ResolveDream -> Res.drawable.potato_card_full
    ZoneAbility.DreamBard -> Res.drawable.bardfox_card_full
    else -> Res.drawable.potato_card_full
}