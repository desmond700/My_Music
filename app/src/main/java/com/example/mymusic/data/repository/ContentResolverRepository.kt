package com.example.mymusic.data.repository

import com.example.mymusic.R
import com.example.mymusic.data.Result
import com.example.mymusic.data.helper.ContentResolverHelper
import com.example.mymusic.data.successOr
import com.example.mymusic.utils.ErrorMessage
import com.example.mymusic.utils.getAlbums
import com.example.mymusic.utils.getArtists
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

interface IMusicRepository {
    fun loadAllAudio()

}

class ContentResolverRepository @Inject constructor(
    private val contentResolverHelper: ContentResolverHelper,
    private val songRepository: SongRepository,
    private val albumRepository: AlbumRepository,
    private val artistRepository: ArtistRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
): IMusicRepository {
    private val scope = CoroutineScope(coroutineDispatcher)
    private var errorMessages = MutableStateFlow(emptyList<ErrorMessage>())

//    @OptIn(ExperimentalCoroutinesApi::class)
//    val allSongs = contentResolverHelper.allSongs.flatMapLatest { songs ->
//        flow {
//            when(songs) {
//                is Result.Success -> {
//                    val songList = songs.successOr(emptyList())
//                    emit(SongResult.Success(songList))
//                }
//                is Result.Error -> {
//                    val errorMessages = errorMessages + ErrorMessage(
//                        id = UUID.randomUUID().mostSignificantBits,
//                        messageId = R.string.load_error,
//                        message = songs.errorMessage
//                    )
//
//                    this@ContentResolverRepository.errorMessages = errorMessages
//
//                    emit(SongResult.Error(errorMessages))
//                }
//            }
//        }
//    }

    init {
        loadAllAudio()
    }

    override fun loadAllAudio() {
        scope.launch {
            when(val result = contentResolverHelper.getAudioData()) {
                is Result.Success -> {
                    val songs = result.successOr(emptyList())
                    songRepository.insertAllSongs(songs)
                    albumRepository.insertAndDeleteAllAlbums(songs.getAlbums())
                    artistRepository.insertAllArtists(songs.getArtists())
                }
                is Result.Error -> {
                    val errorMessages = errorMessages.value + ErrorMessage(
                        id = UUID.randomUUID().mostSignificantBits,
                        messageId = R.string.load_error,
                        message = result.errorMessage
                    )

                    this@ContentResolverRepository.errorMessages.update { errorMessages }
                }
            }
        }
    }
}

sealed class SongResult<out R> {
    data class Success<out T>(val data: T) : SongResult<T>()
    data class Error(val errorMessages: List<ErrorMessage>) : SongResult<Nothing>()
}