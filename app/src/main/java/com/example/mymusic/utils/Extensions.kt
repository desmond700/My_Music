package com.example.mymusic.utils

import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import android.util.Log
import androidx.media3.common.MediaMetadata
import com.example.mymusic.data.room.entities.Album
import com.example.mymusic.data.room.entities.Artist
import com.example.mymusic.data.room.entities.Favourite
import com.example.mymusic.data.room.entities.Song
import com.example.mymusic.model.AudioItem
import java.time.Duration

fun Long.formatDuration(): String {
    val duration = Duration.ofMillis(this)
    val minutes = duration.toMinutes()
    val minuteString = minutes.let {
        if (it < 10) "0$it"
        else it
    }
    val secondsString = duration.minusMinutes(minutes).seconds.let {
        if (it < 10) "0$it"
        else it
    }
    return "$minuteString:$secondsString"
}

fun AudioItem.metadata(): MediaMetadata = MediaMetadata.Builder()
    .setDisplayTitle(this.title)
    .setAlbumArtist(this.albumArtist)
    .setArtist(this.getArtist())
    .setGenre(this.genre)
//    .setRecordingYear(this.year)
    .setComposer(this.composer)
    .setWriter(this.author)
    .build()

fun AudioItem.toFavouriteItem(): Favourite = Favourite(
    musicId = this.id,
    uri = this.path,
)

//fun AudioItem.toRecentItem(): RecentlyPlayed = RecentlyPlayed(
//    musicId = this.id,
//    uri = this.path,
//)

fun Cursor.getCursorMetaData(): Song {
    val cursor = this
    val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))

    // Get Artist info
    val artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))

    // Get Album info
    val album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))

    // Get Album Artist info
    val albumArtist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ARTIST))

    // Get Author info
    val author = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.AUTHOR))

    // Get Genre info
    val genre = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.GENRE))

    // Get Title info
    val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
    Log.d("getMusicMetaData", "title $title")
    // Get Track info
    val track = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))

    // Get Writer info
    val writer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.WRITER))

    // Get Track number info
    val trackNumber = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.CD_TRACK_NUMBER))

    // Get Disc number info
    val discNumber = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISC_NUMBER))

    // Get duration info
    val duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))

    // Get Year media was created
    val year = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR))

    // Get MimeType info
    val mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE))

    // Get Bitrate info
    val bitrate = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.BITRATE))

    // Get Composer info
    val composer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.COMPOSER))

    // Get date created or modified info
    val date = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_MODIFIED))

    // Get Size File size
    val size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))

    // The metadata key to retrieve the music album compilation status.
    val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))

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

    val artworkBlob = try {
//        val metaRetriever = MediaMetadataRetriever()
        metaRetriever.setDataSource(path)
        metaRetriever.embeddedPicture
    }
    catch (e: IllegalArgumentException) {
        Log.d("GetMediaFileData", "metaRetriever error message ${e.message}")
        null
    }

    Log.d("getMusicMetaData", "year $year")

    val song = Song(
//        id = id,
        artistName = artist,
        albumArtist = albumArtist,
        albumName = album,
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
        path = path,
        artworkBlob = artworkBlob
    )

    metaRetriever.release()

    return song
}

fun List<Song>.getAlbums(): List<Album> {
    val songs = this
    val albums = songs.groupBy { song -> song.albumName }

    return albums.values.map { item ->
        Album(
            albumName = songs.first().albumName,
            albumArtist = songs.first().artistName
        )
    }
}

fun List<Song>.getArtists(): List<Artist> {
    val songs = this
    val artist = songs.groupBy { it.artistName }

    return artist.values.map { item ->
        Artist(
            artistName = songs.first().artistName,
            musicId = songs.first().songId,
            uri = songs.first().path
        )
    }
}