package com.example.mymusic

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MusicPlayerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: MusicPlayerApplication
            private set
    }
}