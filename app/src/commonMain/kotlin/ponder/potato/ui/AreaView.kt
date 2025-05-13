package ponder.potato.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringArrayResource
import ponder.potato.model.game.Dream
import ponder.potato.model.game.Zone
import pondui.ui.controls.Card
import pondui.ui.controls.H4
import pondui.ui.controls.Text
import pondui.ui.theme.Pond
import pondui.ui.theme.ProvideBookColors
import potato.app.generated.resources.Res
import potato.app.generated.resources.dream_card_full
import potato.app.generated.resources.dream_description
import kotlin.reflect.KClass

@Composable
fun <T: Zone> AreaView(
    kClass: KClass<T>
) {
    val areaImage = when {
        kClass == Dream::class -> Res.drawable.dream_card_full
        else -> Res.drawable.dream_card_full
    }

    val areaDescription = when {
        kClass == Dream::class -> Res.array.dream_description
        else -> Res.array.dream_description
    }

    Card(
        innerPadding = 0.dp,
    ) {
        Row {
            Image(
                painter = painterResource(areaImage),
                contentDescription = "Image of purchase",
                modifier = Modifier.width(200.dp)
            )
            ProvideBookColors {
                Column(
                    verticalArrangement = Pond.ruler.columnTight,
                    modifier = Modifier.fillMaxWidth()
                        .background(Pond.localColors.surface)
                        .padding(Pond.ruler.innerPadding)
                ) {
                    H4("The Dream", color = Pond.colors.tertiary)
                    for (text in stringArrayResource(areaDescription)) {
                        Text(text)
                    }
                }
            }
        }
    }
}