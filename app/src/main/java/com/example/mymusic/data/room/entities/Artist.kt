package com.example.mymusic.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "artist",
    indices = [Index(value = ["artist_name"], unique = true)]
)
data class Artist (
    @PrimaryKey(autoGenerate = true)
    val artistId: Long = 0,

    @ColumnInfo(name = "artist_name")
    val artistName: String,

    @ColumnInfo(name = "music_uri")
    val uri: String,

    @ColumnInfo(name = "album_art")
    val artwork: ByteArray? = null,

    @ColumnInfo(name = "created_at")
    var createdAt: Long = 0,

    @ColumnInfo(name = "modified_at")
    var modifiedAt: Long = 0,
) {
    companion object {
        val default = Artist(
            artistName = "",
            uri = ""
        )
    }
}