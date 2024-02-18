package com.example.mymusic.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.log10
import kotlin.math.pow

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
    val albumArtist: String? = null,

    val favourite: Boolean = false,

    val author: String? = null,

    val genre: String? = null,

    val writer: String? = null,

    @ColumnInfo(name = "track_number")
    val trackNumber: Int? = null,

    @ColumnInfo(name = "disc_number")
    val discNumber: Int? = null,

    val duration: Long,

    @ColumnInfo(name = "mime_type")
    val mimeType: String? = null,

    val bitrate: Long? = null,

    val composer: String? = null,

    val year: String? = null,

    val path: String,

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val artworkBlob: ByteArray? = null,

    val size: Long,

    @ColumnInfo(name = "created_at")
    var createdAt: Long = 0,

    @ColumnInfo(name = "modified_at")
    var modifiedAt: Long = 0
) {
    fun getArtist(): String {
        return when (artistName != "<unknown>") {
            true -> artistName
            else -> "Unknown Artist"
        }
    }

    fun getSizeString(): String {
        if (size <= 0) return "0"

        val kilobyte = 1024.0
        val units = listOf("B", "KB", "MB", "GB", "TB")
        val unitPosition = (log10(size.toDouble()) / log10(kilobyte)).toInt()
        val unit = units[unitPosition]
        val value = size / kilobyte.pow(unitPosition.toDouble())

        return DecimalFormat("#,##0.#").format(value) + " " + unit
    }

    fun getModifiedDateString(): String? {
        val localDate = LocalDate.parse(modifiedAt.toString(), DateTimeFormatter.BASIC_ISO_DATE)
        return localDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }
}