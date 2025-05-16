package ponder.potato

import com.russhwolf.settings.Settings
import compose.icons.TablerIcons
import compose.icons.tablericons.Bed
import compose.icons.tablericons.ChevronDown
import compose.icons.tablericons.Code
import compose.icons.tablericons.File
import compose.icons.tablericons.Heart
import compose.icons.tablericons.Home
import compose.icons.tablericons.List
import kotlinx.collections.immutable.persistentListOf
import ponder.potato.ui.*
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
        RouteConfig(EntityListRoute::matchRoute) { defaultScreen<EntityListRoute> { EntityListScreen() } },
        RouteConfig(GameDataRoute::matchRoute) { defaultScreen<GameDataRoute> { GameDataScreen() } },
        RouteConfig(ProgramListRoute::matchRoute) { defaultScreen<ProgramListRoute> { ProgramListScreen() } },
        RouteConfig(ProgramProfileRoute::matchRoute) { defaultScreen<ProgramProfileRoute> { ProgramProfileScreen(it) }}
    ),
    doors = persistentListOf(
        PortalDoor(TablerIcons.Home, StartRoute),
        PortalDoor(TablerIcons.Bed, DreamRoute),
        PortalDoor(TablerIcons.ChevronDown, CaveRoute),
        PortalDoor(TablerIcons.List, EntityListRoute),
        PortalDoor(TablerIcons.Code, ProgramListRoute),
        PortalDoor(TablerIcons.File, GameDataRoute, "Data"),
    ),
)

val appSettings: Settings = Settings()