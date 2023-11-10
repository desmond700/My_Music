package com.example.mymusic.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever


fun getMediaFileData(filePath: String) {
    // filePath is of type String which holds the path of file
    val metaRetriever = MediaMetadataRetriever()

    // Get Artist info
    val artistInfo = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)

    // Get Album info
    val albumInfo = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)

    // Get Album Artist info
    val albumArtistInfo = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST)

    // Get Author info
    val authorInfo = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR)

    // Get Genre info
    val genreInfo = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE)

    // Get Title info
    val titleInfo = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)

    // Get Writer info
    val writerInfo = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_WRITER)

    // Get Track number info
    val trackNumber = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER)

    // Get Disc number info
    val discNumber = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DISC_NUMBER)

    // Get duration info
    val durationInfo = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
    val duration = durationInfo?.toLong()

    // Get Year media was created
    val yearInfo = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR)

    // Get MimeType info
    val mimeTypeInfo = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE)

    // Get has image info
    val hasImageInfo = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_IMAGE)
    val hasImage = hasImageInfo.toBoolean()

    // Get Bitrate info
    val bitrateInfo = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)

    // Get Composer info
    val composer = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER)

    // Get date created or modified info
    val date = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE)

    // The metadata key to retrieve the music album compilation status.
    val albumCompilationStatusInfo = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPILATION)

    // Get sample rate info
//    val sampleRateInfo = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_SAMPLERATE)

    // Get bits per sample info
//    val bitsPerSampleInfo = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITS_PER_SAMPLE)

    metaRetriever.setDataSource(filePath)

    val songImageBitmap: Bitmap? = when (val artBytes: ByteArray? = metaRetriever.embeddedPicture) {
        null -> null
        else -> BitmapFactory.decodeByteArray(artBytes, 0, artBytes.size)
    }


}