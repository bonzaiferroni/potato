package ponder.potato

import com.russhwolf.settings.Settings
import compose.icons.TablerIcons
import compose.icons.tablericons.Bed
import compose.icons.tablericons.ChevronDown
import compose.icons.tablericons.File
import compose.icons.tablericons.Heart
import compose.icons.tablericons.Home
import compose.icons.tablericons.List
import kotlinx.collections.immutable.persistentListOf
import ponder.potato.ui.CaveScreen
import ponder.potato.ui.DreamScreen
import ponder.potato.ui.EntityListScreen
import ponder.potato.ui.EntityListView
import ponder.potato.ui.HelloScreen
import ponder.potato.ui.StartScreen
import ponder.potato.ui.ZoneScreen
import pondui.ui.core.PondConfig
import pondui.ui.core.RouteConfig
import pondui.ui.nav.PortalAction
import pondui.ui.nav.PortalDoor
import pondui.ui.nav.defaultScreen

val appConfig = PondConfig(
    name = "Contemplate",
    logo = TablerIcons.Heart,
    home = StartRoute,
    routes = persistentListOf(
        RouteConfig(StartRoute::matchRoute) { defaultScreen<StartRoute> { StartScreen() } },
        RouteConfig(HelloRoute::matchRoute) { defaultScreen<HelloRoute> { HelloScreen() } },
        RouteConfig(DreamRoute::matchRoute) { defaultScreen<DreamRoute> { DreamScreen() } },
        RouteConfig(ZoneRoute::matchRoute) { defaultScreen<ZoneRoute> { ZoneScreen(it) } },
        RouteConfig(CaveRoute::matchRoute) { defaultScreen<CaveRoute> { CaveScreen() } },
        RouteConfig(EntityListRoute::matchRoute) { defaultScreen<EntityListRoute> { EntityListScreen() }}
//        RouteConfig(ExampleListRoute::matchRoute) { defaultScreen<ExampleListRoute> { ExampleListScreen() } },
//        RouteConfig(ExampleProfileRoute::matchRoute) { defaultScreen<ExampleProfileRoute> { ExampleProfileScreen(it) } }
    ),
    doors = persistentListOf(
        PortalDoor(TablerIcons.Home, StartRoute),
        PortalDoor(TablerIcons.Bed, DreamRoute),
        PortalDoor(TablerIcons.ChevronDown, CaveRoute),
        PortalDoor(TablerIcons.List, EntityListRoute),
        PortalAction(TablerIcons.File, "Save") { GameService().save() },
        PortalAction(TablerIcons.File, "Reset") { GameService().reset() },
//        PortalDoor(TablerIcons.YinYang, HelloRoute),
//        PortalDoor(TablerIcons.Rocket, ExampleListRoute),
    ),
)

val appSettings: Settings = Settings()