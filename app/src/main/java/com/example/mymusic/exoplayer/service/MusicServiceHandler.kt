package com.example.mymusic.exoplayer.service

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ShuffleOrder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(DelicateCoroutinesApi::class)
class MusicServiceHandler @Inject constructor(
    private val exoPlayer: ExoPlayer,
    private val coroutineScope: CoroutineScope = GlobalScope,
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
) : Player.Listener {
//    private val _audioState = MutableStateFlow(MusicState())
//    val audioState = _audioState.asStateFlow()
    companion object {
        const val TAG = "MusicServiceHandler"
    }

    private val _currentMediaItemIndex = MutableStateFlow(0)
    val currentMediaItemIndex = _currentMediaItemIndex.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private val _progressState = MutableStateFlow(0L)
    val progressState = _progressState.asStateFlow()

    private val _repeatMode = MutableStateFlow(Player.REPEAT_MODE_OFF)
    val repeatMode = _repeatMode.asStateFlow()

    private val _shuffleState = MutableStateFlow(false)
    val shuffleState = _shuffleState.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration = _duration.asStateFlow()


    private var job: Job? = null

    init {
        exoPlayer.addListener(this)
        Log.d(TAG, "exoPlayer.deviceVolume ${exoPlayer.deviceVolume}")
        Log.d(TAG, "exoPlayer.volume ${exoPlayer.volume}")
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun setMediaItemList(mediaItems: List<MediaItem>, selectedAudioIndex: Int) {
        exoPlayer.setMediaItems(mediaItems)
        exoPlayer.prepare()
        exoPlayer.seekToDefaultPosition(selectedAudioIndex)
//        _audioState.update { it.copy(isPlaying = true) }
        _isPlaying.update { true }
        exoPlayer.playWhenReady = true
        GlobalScope.launch(Dispatchers.Main) {
            startProgressUpdate()
        }
        exoPlayer.play()
    }

    fun setMediaItem(mediaItem: MediaItem) {
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    override fun onVolumeChanged(volume: Float) {
        super.onVolumeChanged(volume)

        Log.d(TAG, "onVolumeChanged $volume")

    }

    override fun onDeviceVolumeChanged(volume: Int, muted: Boolean) {
        super.onDeviceVolumeChanged(volume, muted)

        Log.d(TAG, "onDeviceVolumeChanged $volume")
        Log.d(TAG, "exoPlayer.deviceVolume ${exoPlayer.volume}")
    }
    suspend fun onPlayerEvents(
        playerEvent: PlayerEvent,
        selectedAudioIndex: Int = -1,
        seekPosition: Long = 0
    ) {
        when (playerEvent) {
            PlayerEvent.Backward -> exoPlayer.seekBack()
            PlayerEvent.Forward -> exoPlayer.seekForward()
            PlayerEvent.Previous -> exoPlayer.seekToPreviousMediaItem()
            PlayerEvent.Next -> exoPlayer.seekToNextMediaItem()
            PlayerEvent.PlayPause -> playOrPause()
            PlayerEvent.SeekTo -> exoPlayer.seekTo(seekPosition)
            PlayerEvent.SelectedAudioChange -> {
                when (selectedAudioIndex) {
                    exoPlayer.currentMediaItemIndex -> {
                        playOrPause()
                    }
                    else -> {
                        exoPlayer.seekToDefaultPosition(selectedAudioIndex)
//                        _audioState.update { it.copy(isPlaying = true) }
                        _isPlaying.update { true }
                        exoPlayer.playWhenReady = true
                        startProgressUpdate()
                    }
                }
            }

            PlayerEvent.Stop -> stopProgressUpdate()
            is PlayerEvent.UpdateProgress -> {
//                exoPlayer.seekTo((exoPlayer.duration * playerEvent.newProgress).toLong())
                exoPlayer.seekTo((playerEvent.newProgress).toLong())
            }
            is PlayerEvent.Repeat -> {
                exoPlayer.repeatMode = when (exoPlayer.repeatMode) {
                    Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ONE
                    Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_ALL
                    else -> Player.REPEAT_MODE_OFF
                }

//                _audioState.update { it.copy(repeatMode = exoPlayer.repeatMode) }
                _repeatMode.update { exoPlayer.repeatMode }
            }
            is  PlayerEvent.Shuffle -> {
                when(exoPlayer.shuffleModeEnabled) {
                    true -> exoPlayer.shuffleModeEnabled = false
                    else -> {
                        val randomSeed = 8453L
                        val range = 0..exoPlayer.mediaItemCount
                        val shuffledIndices = range.shuffled().toIntArray()

                        // Set a custom shuffle order for the 5 items currently in the playlist:
                        exoPlayer.setShuffleOrder(
                            ShuffleOrder.DefaultShuffleOrder(exoPlayer.mediaItemCount, randomSeed)
                        )
                        // Enable shuffle mode.
                        exoPlayer.shuffleModeEnabled = true
                    }
                }

//                _audioState.update { it.copy(shuffleModeEnabled = exoPlayer.shuffleModeEnabled) }
                _shuffleState.update { exoPlayer.shuffleModeEnabled }
            }
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            ExoPlayer.STATE_BUFFERING -> {
//                _audioState.update { it.copy(progress = exoPlayer.currentPosition) }
                _progressState.update { exoPlayer.currentPosition }
            }
            ExoPlayer.STATE_READY -> {
//                _audioState.update { it.copy(duration = exoPlayer.duration) }
                _duration.update { exoPlayer.duration }
            }
            Player.STATE_ENDED -> {

            }
            Player.STATE_IDLE -> {

            }
        }
        super.onPlaybackStateChanged(playbackState)
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
//        _audioState.update {
//            it.copy(
//                isPlaying = isPlaying,
//                currentMediaItemIndex = exoPlayer.currentMediaItemIndex
//            )
//        }

        _isPlaying.update { isPlaying }
        _currentMediaItemIndex.update { exoPlayer.currentMediaItemIndex }

        if (isPlaying) {
            coroutineScope.launch(mainDispatcher) {
                startProgressUpdate()
            }
        } else {
            stopProgressUpdate()
        }
    }

    private suspend fun playOrPause() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
            stopProgressUpdate()
        } else {
            exoPlayer.play()
//            _audioState.update { it.copy(isPlaying = true) }
            _isPlaying.update { true }
            startProgressUpdate()
        }
    }

    private suspend fun startProgressUpdate() = job.run {
        while (true) {
            delay(500)
//            _audioState.update { it.copy(progress = exoPlayer.currentPosition) }
            _progressState.update { exoPlayer.currentPosition }
        }
    }

    private fun stopProgressUpdate() {
        job?.cancel()
//        _audioState.update { it.copy(isPlaying = false) }
        _isPlaying.update { false }
    }
}

sealed class PlayerEvent {
    data object PlayPause : PlayerEvent()
    data object SelectedAudioChange : PlayerEvent()
    data object Backward : PlayerEvent()
    data object Previous : PlayerEvent()
    data object Next : PlayerEvent()
    data object Forward : PlayerEvent()
    data object SeekTo : PlayerEvent()
    data object Repeat : PlayerEvent()
    data object Stop : PlayerEvent()
    data object Shuffle : PlayerEvent()
    data class UpdateProgress(val newProgress: Float) : PlayerEvent()
}

data class MusicState (
    val duration: Long = 0L,
    val progress: Long = 0L,
    val isPlaying: Boolean = false,
    val currentMediaItemIndex: Int = 0,
    val shuffleModeEnabled: Boolean = false,
    val repeatMode: Int = Player.REPEAT_MODE_OFF
)