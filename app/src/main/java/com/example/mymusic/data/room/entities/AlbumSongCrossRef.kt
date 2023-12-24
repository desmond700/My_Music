package com.example.mymusic.data.room.entities

import androidx.room.Entity

@Entity(
    tableName = "album_songs",
    primaryKeys = ["albumId", "songId"]
)
class AlbumSongCrossRef(
    val albumId: Long,
    val songId: Long
)