package com.example.mymusic.exoplayer.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.ui.PlayerNotificationManager
import com.example.mymusic.R
import com.example.mymusic.utils.NOTIFICATION_CHANNEL_NAME
import com.example.mymusic.utils.NOTIFICATION_CHANNE_ID
import com.example.mymusic.utils.NOTIFICATION_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MusicNotificationManager @Inject constructor(
    @ApplicationContext val context: Context,
    private val exoPlayer: ExoPlayer
) {
    private val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(context)

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            createNotificationChannel()
        }
    }

    fun startNotificationService(
        mediaSessionService: MediaSessionService,
        mediaSession: MediaSession
    ) {
        buildNotification(mediaSession)
        startForegroundNotificationService(mediaSessionService)
    }

    private fun startForegroundNotificationService(mediaSessionService: MediaSessionService) {
        val notification = Notification.Builder(context, NOTIFICATION_CHANNE_ID)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        mediaSessionService.startForeground(NOTIFICATION_ID, notification)
    }

    @OptIn(UnstableApi::class) private fun buildNotification(mediaSession: MediaSession) {
         PlayerNotificationManager.Builder(
             context,
             NOTIFICATION_ID,
             NOTIFICATION_CHANNE_ID
         )
             .setMediaDescriptionAdapter(
                MusicNotificationAdapter(
                    context = context,
                    pendingIntent = mediaSession.sessionActivity
                )
             )
             .setSmallIconResourceId(R.drawable.microphone)
             .build()
             .apply {
                 setMediaSessionToken(mediaSession.sessionCompatToken)
                 setUseFastForwardActionInCompactView(true)
                 setUseRewindActionInCompactView(true)
                 setUseNextActionInCompactView(true)
                 setPriority(NotificationCompat.PRIORITY_LOW)
                 setPlayer(exoPlayer)
             }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNE_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }
}
