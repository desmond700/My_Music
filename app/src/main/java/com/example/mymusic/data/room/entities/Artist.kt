package com.example.mymusic.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artist")
data class Artist (
    @PrimaryKey(autoGenerate = true)
    val artistId: Long = 0,

    @ColumnInfo(name = "artist_name")
    val artistName: String,

    @ColumnInfo(name = "music_id")
    val musicId: Long,

    @ColumnInfo(name = "music_uri")
    val uri: String,

    @ColumnInfo(name = "created_at")
    var createdAt: Long = 0,

    @ColumnInfo(name = "modified_at")
    var modifiedAt: Long = 0,
)