package com.example.mymusic.data.helper

import android.content.Context
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.WorkerThread
import com.example.mymusic.data.Result
import com.example.mymusic.data.room.entities.Song
import com.example.mymusic.utils.getCursorMetaData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


class ContentResolverHelper @Inject constructor(
    @ApplicationContext val context: Context
) {

    private val contentResolver = context.contentResolver
//    val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
    private var selectionClause: String? =
        "${MediaStore.Audio.Media.IS_MUSIC} = ? AND ${MediaStore.Audio.Media.MIME_TYPE} NOT IN (?, ?, ?)"

    private var selectionArg = arrayOf("1", "audio/amr", "audio/3gpp", "audio/aac")

    private val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"

    private val _allSongs = MutableStateFlow<Result<List<Song>>>(Result.Success(emptyList()))
    val allSongs = _allSongs.asStateFlow()

//    private val projections: Array<String> = arrayOf(
//        MediaStore.Audio.AudioColumns.DISPLAY_NAME,
//        MediaStore.Audio.AudioColumns._ID,
//        MediaStore.Audio.AudioColumns.ARTIST,
//        MediaStore.Audio.AudioColumns.ALBUM,
//        MediaStore.Audio.AudioColumns.DATA,
//        MediaStore.Audio.AudioColumns.DURATION,
//        MediaStore.Audio.AudioColumns.TITLE,
//    )

    init {
        getAudioData()
    }

    @WorkerThread
    fun getAudioData(): Result<List<Song>> {
        //        _allSongs.update { songs }
        return getCursorData()
    }

    private fun getCursorData(): Result<List<Song>> {
         val audioList = mutableListOf<Song>()
         val mCursor = contentResolver.query(
             MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
             null,
             selectionClause,
             selectionArg,
             sortOrder
         )

        if (mCursor == null) {
            Log.d("ContentResolverHelper","getCursorData: Cursor is null")
            return Result.Error(errorMessage = "Cursor is null")
        }

        mCursor.use { cursor ->
            cursor.apply {
                if (count == 0) {
                    return Result.Error(errorMessage = "Cursor Count is Empty")
                }
                else {
                    moveToFirst()
                    do {
                        val song = getCursorMetaData()
                        audioList.add(song)
                    }
                    while (moveToNext())
                }
            }
        }

        return Result.Success(audioList)
    }

//    fun updateAudioArtwork(sArtworkUri: Uri, albumId: Long): Bitmap? {
//        val uri = ContentUris.withAppendedId(sArtworkUri, albumId)
//        var inputStream: InputStream? = null
//
//        return try {
//            inputStream = contentResolver.openInputStream(uri)
//            BitmapFactory.decodeStream(inputStream, null, sBitmapOptions)
//        } catch (ex: FileNotFoundException) {
//            // The album art thumbnail does not actually exist. Maybe the user deleted it, or
//            // maybe it never existed to begin with.
//            var bm: Bitmap? = getArtworkFromFile(context, null, albumId)
//            if (bm != null) {
//                // Put the newly found artwork in the database.
//                // Note that this shouldn't be done for the "unknown" album,
//                // but if this method is called correctly, that won't happen.
//// Load thumbnail of a specific media item.
////                val thumbnail: Bitmap =
////                    contentResolver.loadThumbnail(
////                        uri,
////                        Size(640, 480),
////                        null
////                    )
//
//                // first write it somewhere
//                val file = Environment.getExternalStorageDirectory()
//                    .toString() + "/albumthumbs/" + System.currentTimeMillis().toString()
//                if (ensureFileExists(file)) {
//                    try {
//                        val outstream: OutputStream = FileOutputStream(file)
//                        if (bm.config == null) {
//                            bm = bm.copy(Bitmap.Config.RGB_565, false)
//                            if (bm == null) {
//                                return getDefaultArtwork(context)
//                            }
//                        }
//                        var success = bm.compress(Bitmap.CompressFormat.JPEG, 75, outstream)
//                        outstream.close()
//                        if (success) {
//                            val values = ContentValues()
//                            values.put("album_id", albumId)
//                            values.put("_data", file)
//                            val newUri: Uri? = contentResolver.insert(sArtworkUri, values)
//                            if (newUri == null) {
//                                // Failed to insert in to the database. The most likely
//                                // cause of this is that the item already existed in the
//                                // database, and the most likely cause of that is that
//                                // the album was scanned before, but the user deleted the
//                                // album art from the sd card.
//                                // We can ignore that case here, since the media provider
//                                // will regenerate the album art for those entries when
//                                // it detects this.
//                                success = false
//                            }
//                        }
//                        if (!success) {
//                            val f = File(file)
//                            f.delete()
//                        }
//                    } catch (e: FileNotFoundException) {
//                        Log.e(TAG, "error creating file", e)
//                    } catch (e: IOException) {
//                        Log.e(TAG, "error creating file", e)
//                    }
//                }
//            } else {
//                bm = getDefaultArtwork(context)
//            }
//            bm
//        } finally {
//            try {
//                inputStream?.close()
//            } catch (e: IOException) {
//                Log.d(TAG, "exception: $e")
//            }
//        }
//
//    }

}