package com.example.mymusic.exoplayer.service

import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.example.mymusic.exoplayer.notification.MusicNotificationManager
import kotlinx.coroutines.flow.MutableStateFlow

class MusicService: MediaSessionService() {

    companion object {
        var currentSongDuration = 0L
            private set
    }

    var isRunning = false
    private var isPlayerInitialised = false
    private val currentSong = MutableStateFlow<MediaMetadata?>(null)
    private lateinit var mediaSession: MediaSession
    private lateinit var notificationManager: MusicNotificationManager

    @OptIn(UnstableApi::class) override fun onCreate() {
        super.onCreate()
        val player = ExoPlayer.Builder(this).build()
        mediaSession = MediaSession.Builder(this, player).build()
        MusicServiceHandler(exoPlayer = player)
//        setListener(MusicServiceHandler(exoPlayer = mediaSession.player))
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notificationManager.startNotificationService(
            mediaSession = mediaSession,
            mediaSessionService = this
        )
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession = mediaSession

    override fun onDestroy() {
        super.onDestroy()
        mediaSession.apply {
            release()
            if (player.playbackState != Player.STATE_IDLE) {
                player.seekTo(0)
                player.playWhenReady = false
                player.stop()
            }
        }
    }


}