package com.example.mymusic.model

import android.graphics.Bitmap

data class Artist(
    val name: String = "",
    val artistArt: Bitmap? = null,
    val songs: List<AudioItem> = emptyList()
)