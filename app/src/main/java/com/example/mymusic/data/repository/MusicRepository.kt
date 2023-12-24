package com.example.mymusic.data.repository

import com.example.mymusic.data.Result
import com.example.mymusic.data.helper.ContentResolverHelper
import com.example.mymusic.data.room.entities.Song
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface IMusicRepository {
    suspend fun getAllAudio(): Result<List<Song>>

}

class ContentResolverRepository @Inject constructor(
    private val contentResolverHelper: ContentResolverHelper,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
): IMusicRepository {
    override suspend fun getAllAudio(): Result<List<Song>> = withContext(coroutineDispatcher) {
        val audioList = contentResolverHelper.getAudioData()

        Result.Success(audioList)
    }
}