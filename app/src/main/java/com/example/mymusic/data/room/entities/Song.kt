package com.example.mymusic.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class Song(
    @PrimaryKey
    val songId: Long = 0,

    @ColumnInfo(name = "artist_name")
    val artistName: String,

    val title: String,

    @ColumnInfo(name = "album_name")
    val albumName: String,

    @ColumnInfo(name = "album_artist")
    val albumArtist: String,

    val favourite: Boolean = false,

    val author: String,

    val genre: String,

    val writer: String,

    @ColumnInfo(name = "track_number")
    val trackNumber: Int,

    @ColumnInfo(name = "disc_number")
    val discNumber: Int,

    val duration: Long,

    @ColumnInfo(name = "mime_type")
    val mimeType: String,

    val bitrate: Long,

    val composer: String,

    val year: String,

    val path: String,

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val artworkBlob: ByteArray? = null,

    val size: Long,

    @ColumnInfo(name = "created_at")
    var createdAt: Long = 0,

    @ColumnInfo(name = "modified_at")
    var modifiedAt: Long = 0
)