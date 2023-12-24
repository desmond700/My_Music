package com.example.mymusic.di

import android.content.Context
import com.example.mymusic.data.room.MusicDatabase
import com.example.mymusic.data.room.dao.AlbumDao
import com.example.mymusic.data.room.dao.ArtistDao
import com.example.mymusic.data.room.dao.MusicPlayedAmountDao
import com.example.mymusic.data.room.dao.PlaylistDao
import com.example.mymusic.data.room.dao.SongDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun provideMusicDatabase(@ApplicationContext context: Context): MusicDatabase =
        MusicDatabase.getInstance(context)

    @Provides
    fun provideSongDao(database: MusicDatabase): SongDao = database.songs()

    @Provides
    fun provideAlbumDao(database: MusicDatabase): AlbumDao = database.albums()

    @Provides
    fun provideArtistDao(database: MusicDatabase): ArtistDao = database.artists()

    @Provides
    fun providePlaylistDao(database: MusicDatabase): PlaylistDao = database.playlist()

    @Provides
    fun provideMusicPlayedAmountDao(database: MusicDatabase): MusicPlayedAmountDao = database.musicPlayAmount()
}