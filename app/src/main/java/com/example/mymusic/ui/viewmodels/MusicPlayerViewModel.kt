package com.example.mymusic.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import androidx.palette.graphics.Palette
import com.example.mymusic.data.repository.MusicPlayedAmountRepository
import com.example.mymusic.data.room.entities.Song
import com.example.mymusic.model.AudioItem
import com.example.mymusic.usecases.MusicUseCases
import com.example.mymusic.utils.toBitmap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private val musicDummy = AudioItem(
    0L, "", "", "", "", "", "", "", 0,
    0, null, null, 0, "", null, 0,
    0L, ""
)

data class MusicPlayerViewModelState(
    val currentSong: Song? = null,
    val colorPalette: Palette? = null,
    val repeatMode: Int = Player.REPEAT_MODE_OFF,
    val shuffleModeEnabled: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    private val musicUseCase: MusicUseCases,
    private val musicPlayedAmountRepository: MusicPlayedAmountRepository
//    private val repository: MusicRepository,
//    private val savedStateHandle: SavedStateHandle
): ViewModel() {



    private val _uiState = MutableStateFlow(MusicPlayerViewModelState())
    val uiState = _uiState.asStateFlow()

    private val currentAudioList = musicUseCase.currentAudioList
    val isPlaying = musicUseCase.isPlaying
    val progress = musicUseCase.progressState
    val duration = musicUseCase.duration

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage = _snackbarMessage.asStateFlow()


    init {
        viewModelScope.launch {
            musicUseCase.currentMediaItemIndex.collectLatest { currentMediaItemIndex ->
                Log.d(TAG, "currentMediaItemIndex begin $currentMediaItemIndex")
                val currentSong = currentAudioList.value.let { list ->
                    if (list.isEmpty()) null
                    else list[currentMediaItemIndex]
                }
                Log.d(TAG, "currentMediaItemIndex $currentMediaItemIndex")

                val colorPalette = currentSong?.artworkBlob?.let { artwork ->
                    Palette.from(artwork.toBitmap()).generate()
                }

                _uiState.update { it.copy(
                        currentSong = currentSong,
                        colorPalette = colorPalette
                    )
                }

                currentSong?.let { song ->
                    musicUseCase.updatePlayCount(songId = song.songId)
                }
            }
        }
        viewModelScope.launch {
            musicUseCase.repeatMode.collectLatest { repeatMode ->
                Log.d(TAG, "repeatMode $repeatMode")
                if (_uiState.value.repeatMode != repeatMode) {
                    when (repeatMode) {
                        Player.REPEAT_MODE_OFF -> {
                            _snackbarMessage.update { "Repeat mode off" }
                        }

                        Player.REPEAT_MODE_ONE -> {
                            _snackbarMessage.update { "Repeat mode 1" }
                        }

                        Player.REPEAT_MODE_ALL -> {
                            _snackbarMessage.update { "Repeat mode all" }
                        }
                    }
                    _uiState.update { it.copy(repeatMode = repeatMode) }
                }
            }
        }
        viewModelScope.launch {
            musicUseCase.shuffleState.collectLatest { shuffleMode ->
                Log.d(TAG, "shuffleMode $shuffleMode")
                if (_uiState.value.shuffleModeEnabled != shuffleMode) {
                    when (shuffleMode) {
                        true -> {
                            _snackbarMessage.update { "Shuffle on" }
                        }
                        else -> {
                            _snackbarMessage.update { "Shuffle off" }
                        }
                    }
                    _uiState.update { it.copy(shuffleModeEnabled = shuffleMode) }
                }
            }
        }
    }

//    private fun loadAudioData() {
//        viewModelScope.launch {
//            val audioList = repository.getAllAudio()
//            allMusicList = audioList.successOr(emptyList())
//            setMediaItems()
//        }
//    }

//    private fun setMediaItems() {
//        allMusicList.map { audio ->
//            MediaItem.Builder()
//                .setUri(audio.path)
//                .setMediaMetadata(audio.metadata())
//                .build()
//
//        }.also {
////            musicServiceHandler.setMediaItemList(it)
//        }
//    }

//    private fun calculateProgressValue(currentProgress: Long) {
//        progress =
//            if (currentProgress > 0) ((currentProgress.toFloat() / duration.toFloat()) * 100f)
//            else 0f
//        progressString = currentProgress.formatDuration()
//    }

    fun selectAudioItem(audio: AudioItem) {
        musicUseCase.setAudioItem(audio)
    }

    fun selectAudioItems(audioList: List<AudioItem>) {
//        musicUseCase.setAudioItems(audioList)
    }

    fun onPlayNext() {

    }

    fun onPlayerUIEvents(uiEvents: UIEvents) = viewModelScope.launch {
        musicUseCase.onPlayerUIEvents(uiEvents)
    }

    override fun onCleared() {
        viewModelScope.launch {
            musicUseCase.stopPlayer()
        }
        super.onCleared()
    }

    companion object {
        val TAG = "MusicPlayerViewModel"
    }
}

sealed class UIEvents {
    data object PlayPause : UIEvents()
    data class SelectedAudioChange(val index: Int) : UIEvents()
    data class SeekTo(val position: Float) : UIEvents()
    data object Previous : UIEvents()
    data object Next : UIEvents()
    data object Backward : UIEvents()
    data object Forward : UIEvents()
    data object Repeat : UIEvents()
    data object Shuffle : UIEvents()
    data class UpdateProgress(val newProgress: Float) : UIEvents()
}

sealed class UIState {
    data object Initial : UIState()
    data object Ready : UIState()
}