package com.example.mymusic.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector


sealed class MainRoutes(val route: String) {
    data object Home : MainRoutes("home")
    data object Menu : MainRoutes("menu")
    data object Settings : MainRoutes("settings")
}

sealed class HomeRoutes(val route: String) {
    data object Default : HomeRoutes("default")
    data object Songs : HomeRoutes("songs")
    data object Artists : HomeRoutes("artists")
    data object ArtistSongs : HomeRoutes("artists/{artistId}")
    data object Albums : HomeRoutes("albums")
    data object AlbumSongs : HomeRoutes("albums/{albumId}")
    data object Playlists : HomeRoutes("playlists")
    data object PlaylistDetails : HomeRoutes("playlists/{playlistId}")
    data object AddToPlaylist : HomeRoutes("addToPlaylist/{songId}")
    data object EditArtwork : HomeRoutes("editArtwork/{songId}")
}


enum class BottomBarItemType {
    Menu,
    Home,
    Settings
}

data class BottomBarItem (
    val type: BottomBarItemType,
    val icon: ImageVector,
    val iconDescription: String,
)

val bottomNarItems = listOf(
    BottomBarItem(
        type = BottomBarItemType.Menu,
        icon = Icons.Default.Menu,
        iconDescription = "Bottom bar menu icon"
    ),
    BottomBarItem(
        type = BottomBarItemType.Home,
        icon = Icons.Default.Home,
        iconDescription = "Bottom bar home icon"
    ),
    BottomBarItem(
        type = BottomBarItemType.Settings,
        icon = Icons.Default.Settings,
        iconDescription = "Bottom bar settings icon"
    )
)