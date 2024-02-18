package com.example.mymusic.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "music_played_Amount",
    indices = [
        Index(
            value = ["song_id"],
            unique = true
        ),
        Index(
            value = ["artist_id"],
            unique = true
        ),
        Index(
            value = ["album_id"],
            unique = true
        ),
        Index(
            value = ["playlist_id"],
            unique = true
        )
    ]
)
data class MusicPlayedAmount(
    @PrimaryKey(autoGenerate = true)
    val mostPlayedId: Long = 0,

    @ColumnInfo(name = "song_id")
    val songId: Long? = null,

    @ColumnInfo(name = "artist_id")
    val artistId: Long? = null,

    @ColumnInfo(name = "album_id")
    val albumId: Long? = null,

    @ColumnInfo(name = "playlist_id")
    val playlistId: Long? = null,

    @ColumnInfo(name = "play_count")
    var playCount: Int = 0,

    @ColumnInfo(name = "created_at")
    var createdAt: Long = 0,

    @ColumnInfo(name = "modified_at")
    var modifiedAt: Long = 0
)