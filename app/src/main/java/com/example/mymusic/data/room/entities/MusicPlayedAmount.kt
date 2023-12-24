package com.example.mymusic.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "music_played_Amount")
data class MusicPlayedAmount(
    @PrimaryKey(autoGenerate = true)
    val mostPlayedId: Long,

    val songId: Long,

    @ColumnInfo(name = "play_count")
    var playCount: Int,

    @ColumnInfo(name = "created_at")
    var createdAt: Long = 0,

    @ColumnInfo(name = "modified_at")
    var modifiedAt: Long = 0
)