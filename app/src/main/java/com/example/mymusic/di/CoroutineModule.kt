package com.example.mymusic.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class CoroutineModule {
    @Provides
    fun provideMainDispatcher() = Dispatchers.Main

    @Provides
    fun provideIoDispatcher() = Dispatchers.IO
}