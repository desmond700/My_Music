package com.example.mymusic.usecases

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.media3.common.MediaItem
import com.example.mymusic.data.repository.MusicPlayedAmountRepository
import com.example.mymusic.data.room.entities.MusicPlayedAmount
import com.example.mymusic.data.room.entities.Song
import com.example.mymusic.exoplayer.service.MusicServiceHandler
import com.example.mymusic.exoplayer.service.PlayerEvent
import com.example.mymusic.model.AudioItem
import com.example.mymusic.ui.viewmodels.UIEvents
import com.example.mymusic.utils.ErrorMessage
import com.example.mymusic.utils.metadata
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//enum class MusicUseCasesLibraryItem {
//    Song,
//    Artist,
//    Album,
//    Playlist,
//    None
//}

sealed class MusicUseCasesLibraryItem {
    data object Song : MusicUseCasesLibraryItem()
    data class Artist(val artistId: Long) : MusicUseCasesLibraryItem()
    data class Album(val albumId: Long) : MusicUseCasesLibraryItem()
    data class Playlist(val playlistId: Long) : MusicUseCasesLibraryItem()
    data object None : MusicUseCasesLibraryItem()
}

@OptIn(DelicateCoroutinesApi::class)
class MusicUseCases @Inject constructor(
    private val musicServiceHandler: MusicServiceHandler,
    private val musicPlayedAmountRepository: MusicPlayedAmountRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val savedStateHandle: SavedStateHandle = SavedStateHandle()
) {
    val currentMediaItemIndex = musicServiceHandler.currentMediaItemIndex
    val isPlaying = musicServiceHandler.isPlaying
    val progressState = musicServiceHandler.progressState
    val repeatMode = musicServiceHandler.repeatMode
    val shuffleState = musicServiceHandler.shuffleState
    var duration = musicServiceHandler.duration

    private val _fetchSongErrors = MutableStateFlow(emptyList<ErrorMessage>())
    val fetchSongErrors = _fetchSongErrors.asStateFlow()

    private val _allSongs = MutableStateFlow(emptyList<Song>())
    val allSongs = _allSongs.asStateFlow()

    private var _currentAudioList = MutableStateFlow(emptyList<Song>())
    var currentAudioList = _currentAudioList.asStateFlow()

    private var coroutineScope = CoroutineScope(ioDispatcher)

    @OptIn(SavedStateHandleSaveableApi::class)
    private var currentLibraryItem: MusicUseCasesLibraryItem by savedStateHandle.saveable {
        mutableStateOf(MusicUseCasesLibraryItem.None)
    }

    init {
        Log.d(TAG, "init")
    }


    suspend fun onPlayerUIEvents(uiEvents: UIEvents) {
        when (uiEvents) {
            UIEvents.Backward -> musicServiceHandler.onPlayerEvents(PlayerEvent.Backward)
            UIEvents.Forward -> musicServiceHandler.onPlayerEvents(PlayerEvent.Forward)
            UIEvents.Next -> musicServiceHandler.onPlayerEvents(PlayerEvent.Next)
            UIEvents.Previous -> musicServiceHandler.onPlayerEvents(PlayerEvent.Previous)
            is UIEvents.PlayPause -> {
                musicServiceHandler.onPlayerEvents(PlayerEvent.PlayPause)
            }
            is UIEvents.SeekTo -> {
                musicServiceHandler.onPlayerEvents(
                    PlayerEvent.SeekTo,
                    seekPosition = uiEvents.position.toLong()
                )

//                duration.collect { value ->
//                    musicServiceHandler.onPlayerEvents(
//                        PlayerEvent.SeekTo,
//                        seekPosition = ((value * uiEvents.position) / 100f).toLong()
//                    )
//                }
            }
            is UIEvents.SelectedAudioChange -> {
                musicServiceHandler.onPlayerEvents(
                    PlayerEvent.SelectedAudioChange,
                    selectedAudioIndex = uiEvents.index
                )
            }
            is UIEvents.UpdateProgress -> {
                musicServiceHandler.onPlayerEvents(
                    PlayerEvent.UpdateProgress(uiEvents.newProgress)
                )
//                progress = uiEvents.newProgress
            }
            is UIEvents.Repeat -> {
                musicServiceHandler.onPlayerEvents(PlayerEvent.Repeat)
            }
            is UIEvents.Shuffle -> {
                musicServiceHandler.onPlayerEvents(PlayerEvent.Shuffle)
            }
        }
    }


    suspend fun updatePlayCount(songId: Long) {
        Log.d(TAG, "updatePlayCount")

        val libraryItem =  currentLibraryItem
        Log.d(TAG, "updatePlayCount libraryItem $libraryItem")


        if (libraryItem == MusicUseCasesLibraryItem.None) {
            Log.d(TAG, "updatePlayCount MusicUseCasesLibraryItem.Song libraryItem $libraryItem")
            return;
        }

        val item = when(libraryItem) {
            is MusicUseCasesLibraryItem.Song -> {
                MusicPlayedAmount(songId = songId, playCount = 1)
            }
            is MusicUseCasesLibraryItem.Album -> {
                MusicPlayedAmount(albumId = libraryItem.albumId, playCount = 1)
            }
            is MusicUseCasesLibraryItem.Artist -> {
                MusicPlayedAmount(artistId = libraryItem.artistId, playCount = 1)
            }
            is MusicUseCasesLibraryItem.Playlist -> {
                MusicPlayedAmount(playlistId = libraryItem.playlistId, playCount = 1)
            }
            else -> MusicPlayedAmount()
        }

        Log.d(TAG, "updatePlayCount libraryItem $libraryItem")
        Log.d(TAG, "updatePlayCount item $item")


        Log.d(TAG, "updatePlayCount update")
        coroutineScope.launch(Dispatchers.IO) {
            musicPlayedAmountRepository.update(item)
        }



    }

    fun selectAudioItem(
        audioList: List<Song>,
        audioIndex: Int,
        libraryItem: MusicUseCasesLibraryItem
    ) {
        Log.d(TAG, "selectAudioItem libraryItem $libraryItem")
        currentLibraryItem = libraryItem

        when (audioList) {
            currentAudioList -> {
                coroutineScope.launch(Dispatchers.Main) {
                    onPlayerUIEvents(UIEvents.SelectedAudioChange(audioIndex))
                }
            }
            else -> {
                setAudioItems(audioList, audioIndex)
            }
        }

        Log.d(TAG, "selectAudioItem libraryItem after $currentLibraryItem")

    }

    fun setAudioItem(audio: AudioItem) {
        val mediaItem = MediaItem.fromUri(audio.path)
        musicServiceHandler.setMediaItem(mediaItem)
    }

    private fun setAudioItems(audioList: List<Song>, audioIndex: Int) {
        _currentAudioList.value = audioList

        val mediaItems = audioList.map { audio ->
            MediaItem
                .Builder()
                .setUri(audio.path)
                .setMediaMetadata(audio.metadata())
                .build()
        }
        musicServiceHandler.setMediaItemList(mediaItems, audioIndex)
    }

    suspend fun stopPlayer() {
        musicServiceHandler.onPlayerEvents(PlayerEvent.Stop)
    }

    companion object {
        const val TAG = "MusicUseCases"
    }
}