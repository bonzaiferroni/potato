package ponder.potato.ui

import androidx.compose.runtime.Composable
import ponder.potato.StartRoute
import pondui.ui.controls.RouteButton
import pondui.ui.controls.Text
import pondui.ui.nav.Scaffold

@Composable
fun HelloScreen() {
    Scaffold {
        Text("Hello world!")
        RouteButton("Go to start") { StartRoute }
    }
}