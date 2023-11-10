package com.example.mymusic.utils


sealed class MainRoutes(val route: String) {
    data object Home : MainRoutes("home")
    data object Search : MainRoutes("search")
}

sealed class HomeRoutes(val route: String) {
    data object Default : HomeRoutes("default")
    data object Songs : HomeRoutes("songs")
    data object Artists : HomeRoutes("artists")
    data object Albums : HomeRoutes("albums")
    data object Playlists : HomeRoutes("playlists")
}