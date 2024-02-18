package com.example.mymusic.utils

import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.database.getIntOrNull
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import androidx.media3.common.MediaMetadata
import com.example.mymusic.data.room.entities.Album
import com.example.mymusic.data.room.entities.Artist
import com.example.mymusic.data.room.entities.Song
import com.example.mymusic.data.room.relationship.PlaylistWithSongs
import java.time.Duration


fun String.quantityAware(count: Int): String {
    return if (count == 1) this else this.dropLast(1)
}

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

fun Song.metadata(): MediaMetadata = MediaMetadata.Builder()
    .setDisplayTitle(this.title)
    .setAlbumArtist(this.albumArtist)
    .setArtist(this.getArtist())
    .setGenre(this.genre)
//    .setRecordingYear(this.year)
    .setComposer(this.composer)
    .setWriter(this.author)
    .build()



//fun AudioItem.toRecentItem(): RecentlyPlayed = RecentlyPlayed(
//    musicId = this.id,
//    uri = this.path,
//)

fun ByteArray.toBitmap(): Bitmap {
    val bytes = this
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

fun ByteArray.toImageBitmap(): ImageBitmap {
    val bytes = this
    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    return bitmap.asImageBitmap()
}

fun Cursor.getCursorMetaData(): Song {
    val cursor = this
    val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns._ID))

    // Get Artist info
    val artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST))

    // Get Album info
    val album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM))

    // Get Album Artist info
    val albumArtist: String? = cursor.getStringOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ARTIST))

    // Get Author info
    val author = cursor.getStringOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.AUTHOR))

    // Get Genre info
    val genre = cursor.getStringOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.GENRE))

    // Get Title info
    val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE))
    Log.d("getMusicMetaData", "title $title")

    // Get Writer info
    val writer = cursor.getStringOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.WRITER))

    // Get Track number info
    val trackNumber = cursor.getIntOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.CD_TRACK_NUMBER))

    // Get Disc number info
    val discNumber = cursor.getIntOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DISC_NUMBER))

    // Get duration info
    val duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION))

    // Get Year media was created
    val year: String? = cursor.getStringOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.YEAR))
    Log.d("getMusicMetaData", "year $year")

    // Get MimeType info
    val mimeType = cursor.getStringOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.MIME_TYPE))

    // Get Bitrate info
    val bitrate = cursor.getLongOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.BITRATE))

    // Get Composer info
    val composer = cursor.getStringOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.COMPOSER))

    // Get date created or modified info
    val date = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATE_MODIFIED))

    // Get Size File size
    val size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.SIZE))

    // The metadata key to retrieve the music album compilation status.
    val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA))

    // Get sample rate info
//    val sampleRateInfo = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_SAMPLERATE)

    // Get bits per sample info
//    val bitsPerSampleInfo = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITS_PER_SAMPLE)
    val metaRetriever = MediaMetadataRetriever()

    val artworkBlob = try {
        metaRetriever.setDataSource(path)
        metaRetriever.embeddedPicture
    }
    catch (e: IllegalArgumentException) {
        Log.d("GetMediaFileData", "metaRetriever error message ${e.message}")
        null
    }

    val song = Song(
        songId = id,
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

fun PlaylistWithSongs.getArtworks(): List<ImageBitmap>? {
    val songs = this.songs
    val bitmaps = mutableListOf<ImageBitmap>()

     songs.forEach label@{ song ->
        if (bitmaps.count() >= 4) return@label

        if (song.artworkBlob != null) {
            bitmaps.add(song.artworkBlob.toImageBitmap())
        }
    }

    return bitmaps.ifEmpty { null }
}

fun List<Song>.getArtworkBlob(): ByteArray? {
    return this.find { it.artworkBlob != null }?.artworkBlob
}

fun List<Song>.getAlbums(): List<Album> {
    val songs = this
    val albums = songs.groupBy { song -> song.albumName }

    return albums.values.map { songList ->
        Album(
            albumName = songList.first().albumName,
            albumArtist = songList.first().artistName,
            albumArt = songList.getArtworkBlob(),
        )
    }
}

fun List<Song>.getArtists(): List<Artist> {
    val songs = this
    val artist = songs.groupBy { it.artistName }

    return artist.values.mapIndexed { index, songList  ->
        Artist(
            artistId = index.toLong(),
            artistName = songList.first().artistName,
            uri = songList.first().path,
            artwork = songList.getArtworkBlob()
        )
    }
}