package com.example.mymusic.model

import android.graphics.Bitmap

data class Album(
    val name: String = "",
    val artist: String = "",
    val albumArt: Bitmap? = null,
    val songs: List<AudioItem> = emptyList()
)