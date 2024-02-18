package com.example.mymusic.di

import android.content.Context
import androidx.datastore.core.DataStore
import com.example.mymusic.data.datastore.AppSettings
import com.example.mymusic.data.datastore.settingsDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatastoreModule {
    @Provides
    @Singleton
    fun provideAppSettingsDatastore(@ApplicationContext context: Context): DataStore<AppSettings> =
        context.settingsDataStore
}