package com.example.mymusic.di

import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.session.MediaSession
import com.example.mymusic.data.helper.ContentResolverHelper
import com.example.mymusic.data.repository.AlbumRepository
import com.example.mymusic.data.repository.MusicPlayedAmountRepository
import com.example.mymusic.data.repository.PlaylistRepository
import com.example.mymusic.exoplayer.notification.MusicNotificationManager
import com.example.mymusic.exoplayer.service.MusicServiceHandler
import com.example.mymusic.usecases.AlbumUseCases
import com.example.mymusic.usecases.MusicUseCases
import com.example.mymusic.usecases.PlaylistUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MediaModule {

    @Provides
    @Singleton
    fun provideAudioAttributes(): AudioAttributes = AudioAttributes.Builder()
        .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()

    @Provides
    @Singleton
    @UnstableApi
    fun provideExoPlayer(
        @ApplicationContext context: Context,
        audioAttributes: AudioAttributes
    ): ExoPlayer = ExoPlayer.Builder(context)
        .setAudioAttributes(audioAttributes, true)
        .setHandleAudioBecomingNoisy(true)
        .setTrackSelector(DefaultTrackSelector(context))
        .build()

    @Provides
    @Singleton
    fun provideMediaSession(
        @ApplicationContext context: Context,
        player: ExoPlayer
    ): MediaSession = MediaSession.Builder(context, player).build()

    @Provides
    @Singleton
    fun provideNotificationManager(
        @ApplicationContext context: Context,
        player: ExoPlayer
    ): MusicNotificationManager = MusicNotificationManager(
        context = context,
        exoPlayer = player
    )

    @Provides
    @Singleton
    fun provideServiceHandler(exoPlayer: ExoPlayer): MusicServiceHandler =
        MusicServiceHandler(exoPlayer)

    @Provides
    @Singleton
    fun provideMusicUseCase(
        musicServiceHandler: MusicServiceHandler,
        musicPlayedAmountRepository: MusicPlayedAmountRepository
    ): MusicUseCases = MusicUseCases(
        musicServiceHandler,
        musicPlayedAmountRepository
    )

    @Provides
    @Singleton
    fun provideAlbumUsCases(
        albumRepository: AlbumRepository
    ): AlbumUseCases = AlbumUseCases(albumRepository)

    @Provides
    @Singleton
    fun providePlaylist(
        playlistRepository: PlaylistRepository
    ): PlaylistUseCases = PlaylistUseCases(playlistRepository)

    @Provides
    @Singleton
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolverHelper =
        ContentResolverHelper(context = context)
}