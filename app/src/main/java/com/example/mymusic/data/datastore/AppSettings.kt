package com.example.mymusic.data.datastore

import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val darkMode: Boolean,
    val playImmediately: Boolean
) {

    companion object {
        val default = AppSettings(
            darkMode = true,
            playImmediately = false
        )
    }
}

sealed class AppSettingsState {
    // Show loading UI
    data object Loading: AppSettingsState()
    // We have settings and we can do something
    class Loaded(val data: AppSettings) : AppSettingsState()
    // Error occurred during the loading.
    class Error(val message: String?): AppSettingsState()
}