package com.example.mymusic.usecases

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.media3.common.MediaItem
import com.example.mymusic.exoplayer.service.MusicServiceHandler
import com.example.mymusic.exoplayer.service.PlayerEvent
import com.example.mymusic.model.Album
import com.example.mymusic.model.AudioItem
import com.example.mymusic.ui.viewmodels.UIEvents
import com.example.mymusic.ui.viewmodels.UIState
import com.example.mymusic.utils.formatDuration
import com.example.mymusic.utils.metadata
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@OptIn(DelicateCoroutinesApi::class)
class MusicUseCases @Inject constructor(
    private val musicServiceHandler: MusicServiceHandler,
) {
    private val _uiState: MutableStateFlow<UIState> = MutableStateFlow(UIState.Initial)
    val audioState = musicServiceHandler.audioState

    var duration by mutableLongStateOf(0L)

    var progress by mutableFloatStateOf(0f)
    var progressString by mutableStateOf("00:00")
    var isPlaying by mutableStateOf(false)
    var currentSelectedAudio = mutableStateOf<AudioItem?>(null)
    var allMusicList by mutableStateOf(emptyList<AudioItem>())
    var albumList by mutableStateOf(emptyList<Album>())
    var artistList by mutableStateOf(emptyList<Album>())
    var playlists by mutableStateOf(emptyList<Album>())
    private var _currentAudioList = MutableStateFlow(emptyList<AudioItem>())
    var currentAudioList = _currentAudioList.asStateFlow()
    private var coroutineScope = CoroutineScope(Dispatchers.IO)

//    val timePassed = flow {
//        while (true) {
//            val duration = audioState.let { progress }
//
//            emit(duration)
//            delay(1000L)
//        }
//    }


    private fun calculateProgressValue(currentProgress: Long) {
        progress =
            if (currentProgress > 0) ((currentProgress.toFloat() / duration.toFloat()) * 100f)
            else 0f
        progressString = currentProgress.formatDuration()
        Log.d("MusicUseCases", "calculateProgressValue progress $progress")
        Log.d("MusicUseCases", "calculateProgressValue progressString $progressString")
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
                    seekPosition = ((duration * uiEvents.position) / 100f).toLong()
                )
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
                progress = uiEvents.newProgress
            }
            is UIEvents.Repeat -> {
                musicServiceHandler.onPlayerEvents(PlayerEvent.Repeat)
            }
            is UIEvents.Shuffle -> {
                musicServiceHandler.onPlayerEvents(PlayerEvent.Shuffle)
            }
        }
    }

    fun setAudioItem(audio: AudioItem) {
        val mediaItem = MediaItem.fromUri(audio.path)
        musicServiceHandler.setMediaItem(mediaItem)
    }

    fun setAudioItems(audioList: List<AudioItem>, audioIndex: Int) {
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
}