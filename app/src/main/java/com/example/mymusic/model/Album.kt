package com.example.mymusic.model

data class AlbumModel(
    val albumName: String,
    val albumArtist: String,
    val albumArt: ByteArray? = null
)