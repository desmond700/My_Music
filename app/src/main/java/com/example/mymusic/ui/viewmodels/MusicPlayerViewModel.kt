package com.example.mymusic.ui.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import com.example.mymusic.model.AudioItem
import com.example.mymusic.usecases.MusicUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private val musicDummy = AudioItem(
    0L, "", "", "", "", "", "", "", 0,
    0, null, null, 0, "", null, 0,
    0L, ""
)

data class MusicPlayerViewModelState(
    val duration: Long = 0L,
    val progress: Long = 0L,
    val isPlaying: Boolean = false,
    val currentSong: AudioItem? = null,
    val repeatMode: Int = Player.REPEAT_MODE_OFF,
    val shuffleModeEnabled: Boolean = false
)

@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    private val musicUseCase: MusicUseCases,
//    private val repository: MusicRepository,
//    private val savedStateHandle: SavedStateHandle
): ViewModel() {

//    var progress = musicUseCase.progress
//    var currentSelectedAudio = musicUseCase.currentSelectedAudio
    val currentAudioList = musicUseCase.currentAudioList
    var playlists = musicUseCase.playlists

    private val _uiState = mutableStateOf(MusicPlayerViewModelState())
    val uiState: State<MusicPlayerViewModelState> = _uiState

    init {
//        loadAudioData()
    }

    init {
        viewModelScope.launch {
            musicUseCase.audioState.collectLatest { mediaState ->
                _uiState.value = uiState.value.copy(
                    duration = mediaState.duration,
                    progress = mediaState.progress,
                    isPlaying = mediaState.isPlaying,
                    currentSong = currentAudioList.value.let { list ->
                        if (list.isEmpty()) null
                        else list[mediaState.currentMediaItemIndex]
                    },
                    repeatMode = mediaState.repeatMode,
                    shuffleModeEnabled = mediaState.shuffleModeEnabled
                )
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