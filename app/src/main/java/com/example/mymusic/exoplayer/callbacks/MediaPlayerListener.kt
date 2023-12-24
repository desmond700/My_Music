 package com.example.mymusic.exoplayer.callbacks

import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.mymusic.exoplayer.service.MusicService

@UnstableApi
class MediaPlayerListener constructor(private val musicService: MusicService) : Player.Listener {
    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
//        if (playbackState == Player.STATE_READY)
//            musicService.stopForeground()

        val stateString: String = when (playbackState) {
            Player.STATE_IDLE -> "ExoPlayer.STATE_IDLE      -"
            Player.STATE_BUFFERING -> "ExoPlayer.STATE_BUFFERING -"
            Player.STATE_READY -> "ExoPlayer.STATE_READY     -"
            Player.STATE_ENDED -> "ExoPlayer.STATE_ENDED     -"
            else -> "UNKNOWN_STATE             -"
        }
        Log.d("MediaPlayerListener", "onPlaybackStateChanged changed state to $stateString")
    }
}