package ponder.potato

import compose.icons.TablerIcons
import compose.icons.tablericons.Bed
import compose.icons.tablericons.Heart
import compose.icons.tablericons.Home
import compose.icons.tablericons.Rocket
import compose.icons.tablericons.YinYang
import kotlinx.collections.immutable.persistentListOf
import ponder.potato.ui.DreamScreen
import ponder.potato.ui.ExampleListScreen
import ponder.potato.ui.ExampleProfileScreen
import ponder.potato.ui.HelloScreen
import ponder.potato.ui.StartScreen
import pondui.ui.core.PondConfig
import pondui.ui.core.RouteConfig
import pondui.ui.nav.PortalDoor
import pondui.ui.nav.defaultScreen

val appConfig = PondConfig(
    name = "Contemplate",
    logo = TablerIcons.Heart,
    home = StartRoute,
    routes = persistentListOf(
        RouteConfig(StartRoute::matchRoute) { defaultScreen<StartRoute> { StartScreen() } },
        RouteConfig(HelloRoute::matchRoute) { defaultScreen<HelloRoute> { HelloScreen() } },
        RouteConfig(DreamRoute::matchRoute) { defaultScreen<DreamRoute> { DreamScreen() }}
//        RouteConfig(ExampleListRoute::matchRoute) { defaultScreen<ExampleListRoute> { ExampleListScreen() } },
//        RouteConfig(ExampleProfileRoute::matchRoute) { defaultScreen<ExampleProfileRoute> { ExampleProfileScreen(it) } }
    ),
    doors = persistentListOf(
        PortalDoor(TablerIcons.Home, StartRoute),
        PortalDoor(TablerIcons.Bed, DreamRoute),
//        PortalDoor(TablerIcons.YinYang, HelloRoute),
//        PortalDoor(TablerIcons.Rocket, ExampleListRoute),
    ),
)