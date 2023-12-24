package com.example.mymusic.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.util.Log
import com.example.mymusic.utils.formatDuration
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.log10
import kotlin.math.pow

data class AudioItem (
    val id: Long,
    private val artist: String?,
    val album: String,
    val albumArtist: String?,
    val author: String?,
    val genre: String?,
    val title: String,
    val writer: String?,
    val trackNumber: Int?,
    val discNumber: Int?,
    private val duration: Long?,
    val mimeType: String?,
    val bitrate: Long?,
    val composer: String?,
    val year: String?,
    val modifiedAt: Long?,
    private val size: Long,
    val path: String
) {
    fun getArtist(): String {
        return when (artist != null && artist != "<unknown>") {
            true -> artist
            else -> "Unknown Artist"
        }
    }

    fun getDuration(): String {
        if (duration == null) return "00:00"

        return duration.formatDuration()
    }

    fun getArtworkByteArray(): ByteArray? {
        return try {
            val metaRetriever = MediaMetadataRetriever()
            metaRetriever.setDataSource(path)
            metaRetriever.embeddedPicture
        }
        catch (e: IllegalArgumentException) {
            Log.d("GetMediaFileData", "metaRetriever error message ${e.message}")
            null
        }
    }

    fun getArtworkBitmap(): Bitmap? {
        return when (val artBytes: ByteArray? = getArtworkByteArray()) {
            null -> null
            else -> BitmapFactory.decodeByteArray(artBytes, 0, artBytes.size)
        }
    }

    fun getSize(): String {
        if (size <= 0) return "0"

        val kilobyte = 1024.0
        val units = listOf("B", "KB", "MB", "GB", "TB")
        val unitPosition = (log10(size.toDouble()) / log10(kilobyte)).toInt()
        val unit = units[unitPosition]
        val value = size / kilobyte.pow(unitPosition.toDouble())

        return DecimalFormat("#,##0.#").format(value) + " " + unit
    }

    fun getModifiedAt(): String? {
        if (modifiedAt == null) return null;

        val localDate = LocalDate.parse(modifiedAt.toString(), DateTimeFormatter.BASIC_ISO_DATE)
        return localDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }

}