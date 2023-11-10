package com.example.mymusic

import android.app.Application
import com.example.mymusic.data.AppContainerImpl
import com.example.mymusic.data.IAppContainer
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MusicPlayerApplication : Application() {

    lateinit var container: IAppContainer
    override fun onCreate() {
        super.onCreate()
        instance = this
        container = AppContainerImpl()
    }

    companion object {
        lateinit var instance: MusicPlayerApplication
            private set
    }
}