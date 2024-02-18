package com.example.mymusic.utils

enum class HomeScreenSectionType {
    SONGS,
    ARTISTS,
    ALBUMS,
    PLAYLISTS
}

data class HomeScreenSection (
    val label: String,
    val subLabel: Int,
    val icon: Int,
    val type: HomeScreenSectionType
)