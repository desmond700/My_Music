package com.example.mymusic.utils

import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.provider.MediaStore.Audio.Media
import android.util.Log
import com.example.mymusic.model.AudioItem


fun getMusicMetaData(cursor: Cursor): AudioItem {
    val id = cursor.getLong(cursor.getColumnIndexOrThrow(Media._ID))

    // Get Artist info
    val artist = cursor.getString(cursor.getColumnIndexOrThrow(Media.ARTIST))

    // Get Album info
    val album = cursor.getString(cursor.getColumnIndexOrThrow(Media.ALBUM))

    // Get Album Artist info
    val albumArtist = cursor.getString(cursor.getColumnIndexOrThrow(Media.ALBUM_ARTIST))

    // Get Author info
    val author = cursor.getString(cursor.getColumnIndexOrThrow(Media.AUTHOR))

    // Get Genre info
    val genre = cursor.getString(cursor.getColumnIndexOrThrow(Media.GENRE))

    // Get Title info
    val title = cursor.getString(cursor.getColumnIndexOrThrow(Media.TITLE))
    Log.d("getMusicMetaData", "title $title")
    // Get Track info
    val track = cursor.getString(cursor.getColumnIndexOrThrow(Media.DISPLAY_NAME))

    // Get Writer info
    val writer = cursor.getString(cursor.getColumnIndexOrThrow(Media.WRITER))

    // Get Track number info
    val trackNumber = cursor.getInt(cursor.getColumnIndexOrThrow(Media.CD_TRACK_NUMBER))

    // Get Disc number info
    val discNumber = cursor.getInt(cursor.getColumnIndexOrThrow(Media.DISC_NUMBER))

    // Get duration info
    val duration = cursor.getLong(cursor.getColumnIndexOrThrow(Media.DURATION))

    // Get Year media was created
    val year = cursor.getString(cursor.getColumnIndexOrThrow(Media.YEAR))

    // Get MimeType info
    val mimeType = cursor.getString(cursor.getColumnIndexOrThrow(Media.MIME_TYPE))

    // Get Bitrate info
    val bitrate = cursor.getLong(cursor.getColumnIndexOrThrow(Media.BITRATE))

    // Get Composer info
    val composer = cursor.getString(cursor.getColumnIndexOrThrow(Media.COMPOSER))

    // Get date created or modified info
    val date = cursor.getLong(cursor.getColumnIndexOrThrow(Media.DATE_MODIFIED))

    // Get Size File size
    val size = cursor.getLong(cursor.getColumnIndexOrThrow(Media.SIZE))

    // The metadata key to retrieve the music album compilation status.
    val path = cursor.getString(cursor.getColumnIndexOrThrow(Media.DATA))

    // Get sample rate info
//    val sampleRateInfo = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_SAMPLERATE)

    // Get bits per sample info
//    val bitsPerSampleInfo = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITS_PER_SAMPLE)
    val metaRetriever = MediaMetadataRetriever()
    val songImageBitmap: Bitmap? = try {
        metaRetriever.setDataSource(path)

        when (val artBytes: ByteArray? = metaRetriever.embeddedPicture) {
            null -> null
            else -> BitmapFactory.decodeByteArray(artBytes, 0, artBytes.size)
        }
    }
    catch (e: IllegalArgumentException) {
        Log.d("GetMediaFileData", "metaRetriever error message ${e.message}")
        Log.d("GetMediaFileData", "metaRetriever error cause ${e.cause}")
        Log.d("GetMediaFileData", "metaRetriever error stacktrace ${e.stackTraceToString()}")
        null
    }

    Log.d("getMusicMetaData", "year $year")

    val music = AudioItem(
        id = id,
        artist = artist,
        albumArtist = albumArtist,
        album = album,
        author = author,
        genre = genre,
        title = title,
        writer = writer,
        trackNumber = trackNumber,
        discNumber = discNumber,
        duration = duration,
        mimeType = mimeType,
        bitrate = bitrate,
        composer = composer,
        year = year,
        modifiedAt = date,
        size = size,
        path = path
    )

    metaRetriever.release()

    return music
}