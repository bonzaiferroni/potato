package ponder.potato

import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.ui.tooling.preview.Preview

import pondui.io.ProvideUserContext
import pondui.ui.core.PondApp
import pondui.ui.nav.NavRoute
import pondui.ui.theme.ProvideTheme
import pondui.ui.theme.defaultTheme
import pondui.ui.theme.useFamily
import potato.app.generated.resources.Inter_18pt_Light
import potato.app.generated.resources.Inter_18pt_Regular
import potato.app.generated.resources.Inter_24pt_Light
import potato.app.generated.resources.Inter_28pt_Light
import potato.app.generated.resources.Res

@Composable
@Preview
fun App(
    changeRoute: (NavRoute) -> Unit,
    exitApp: (() -> Unit)?,
) {
    ProvideTheme(
        theme = defaultTheme(
            baseFont = useFamily(Res.font.Inter_18pt_Regular),
            h1Font = useFamily(Res.font.Inter_28pt_Light, FontWeight.Light),
            h2Font = useFamily(Res.font.Inter_24pt_Light, FontWeight.Light),
            h4Font = useFamily(Res.font.Inter_18pt_Regular),
        )
    ) {
        ProvideGame {
            PondApp(
                config = appConfig,
                changeRoute = changeRoute,
                exitApp = exitApp
            )
        }
    }
}

