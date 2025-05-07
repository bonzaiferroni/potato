package ponder.potato.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import pondui.ui.controls.*
import pondui.ui.theme.Pond
import pondui.utils.darken
import pondui.utils.format

@Composable
fun LabelValue(
    label: String,
    value: Float,
    decimals: Int = 1,
    color: Color = Pond.colors.secondary.darken(.2f),
    modifier: Modifier = Modifier
) {
    val animatedValue by animateFloatAsState(value)

    Row(verticalAlignment = Alignment.Bottom, modifier = modifier) {
        Label("$label: ", modifier = Modifier.alignByBaseline())
        Text(animatedValue.format(decimals), modifier = Modifier.alignByBaseline(), color = color)
    }
}

@Composable
fun LabelValue(
    label: String,
    value: String,
    color: Color = Pond.colors.secondary.darken(.2f),
    modifier: Modifier = Modifier
) {
    Row(verticalAlignment = Alignment.Bottom, modifier = modifier) {
        Label("$label: ", modifier = Modifier.alignByBaseline())
        Text(value, modifier = Modifier.alignByBaseline(), color = color)
    }
}

@Composable
fun ValueOfMax(
    value: Int,
    max: Int,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Text(value.toString())
        Text(
            " of $max", color = Pond.localColors.contentDim
        )
    }
}