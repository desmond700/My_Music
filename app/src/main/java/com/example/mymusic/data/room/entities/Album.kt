package com.example.mymusic.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "albums")
data class Album(
    @PrimaryKey(autoGenerate = true)
    val albumId: Long = 0,

    @ColumnInfo(name = "album_name")
    val albumName: String,

    @ColumnInfo(name = "album_artist")
    val albumArtist: String,

    @ColumnInfo(name = "album_art")
    val albumArt: ByteArray? = null,

    @ColumnInfo(name = "created_at")
    var createdAt: Long = 0,

    @ColumnInfo(name = "modified_at")
    var modifiedAt: Long = 0
) {
    companion object {
        val default = Album(
            albumName = "",
            albumArtist = "",
            albumArt = null,
        )
    }
}