package com.example.mymusic.utils

import com.example.mymusic.R

enum class HomeScreenSectionType {
    SONGS,
    ARTISTS,
    ALBUMS,
    PLAYLISTS
}

data class HomeScreenSection (
    val label: String,
    val icon: Int,
    val type: HomeScreenSectionType
)

val homeScreenSections = listOf(
    HomeScreenSection(
        label = "Songs",
        icon = R.drawable.music,
        type = HomeScreenSectionType.SONGS
    ),
    HomeScreenSection(
        label = "Artists",
        icon = R.drawable.account_music,
        type = HomeScreenSectionType.ARTISTS
    ),
    HomeScreenSection(
        label = "Albums",
        icon = R.drawable.album,
        type = HomeScreenSectionType.ALBUMS
    ),
    HomeScreenSection(
        label = "Playlists",
        icon = R.drawable.playlist_music,
        type = HomeScreenSectionType.PLAYLISTS
    )
)