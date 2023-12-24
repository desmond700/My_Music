package com.example.mymusic.data.helper

import android.content.Context
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.WorkerThread
import com.example.mymusic.data.room.entities.Song
import com.example.mymusic.utils.getCursorMetaData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ContentResolverHelper @Inject constructor(
    @ApplicationContext val context: Context
) {

//    val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
    private var selectionClause: String? =
        "${MediaStore.Audio.Media.IS_MUSIC} = ? AND ${MediaStore.Audio.Media.MIME_TYPE} NOT IN (?, ?, ?)"

    private val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"

    @WorkerThread
    fun getAudioData(): List<Song> = getCursorData()

    private fun getCursorData(): MutableList<Song> {
         val audioList = mutableListOf<Song>()
         val mCursor = context.contentResolver.query(
             MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
             null,
             selectionClause,
             null,
             sortOrder
         )
        Log.d("ContentResolverHelper","getCursorData: Cursor $mCursor")

        mCursor?.use { cursor ->
            cursor.apply {
                if (count == 0) {
                    Log.d("ContentResolverHelper","getCursorData: Cursor is Empty")
                    Log.d("ContentResolverHelper","getCursorData: cursor.moveToNext()  ${moveToNext()}")
                }
                else {
                    do {
                        val song = getCursorMetaData()
                        audioList.add(song)
                    }
                    while (moveToNext())
                }
            }
        }

        return audioList
    }
}