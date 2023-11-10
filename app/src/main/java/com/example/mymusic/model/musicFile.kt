package com.example.mymusic.model

data class MusicFile (
    val name: String,
    val modifiedAt: Long = 0,
    val size: Long = 0,
    val path: String = ""
)