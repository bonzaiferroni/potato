package ponder.potato.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import pondui.ui.controls.Text

@Composable
fun FpsIndicator(modifier: Modifier = Modifier) {
    var lastFrameTime by remember { mutableStateOf(0L) }
    var fps by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            val frameTime = withFrameNanos { it }
            if (lastFrameTime != 0L) {
                val delta = frameTime - lastFrameTime
                fps = (1_000_000_000L / delta).toInt()
            }
            lastFrameTime = frameTime
        }
    }

    Text(
        text = "$fps fps",
        modifier = modifier,
    )
}