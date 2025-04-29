package ponder.potato.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.*
import kabinet.utils.toMetricString
import pondui.ui.controls.Text
import pondui.utils.format

@Composable
fun AnimatedQuantity(
    quantity: Double
) = AnimatedQuantity(quantity.toFloat())

@Composable
fun AnimatedQuantity(
    quantity: Float
) {
    val animatedQuantity by animateFloatAsState(quantity)
    Text(animatedQuantity.toMetricString())
}