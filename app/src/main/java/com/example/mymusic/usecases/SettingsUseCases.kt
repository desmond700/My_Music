package com.example.mymusic.usecases

import androidx.datastore.core.DataStore
import com.example.mymusic.data.datastore.AppSettings
import com.example.mymusic.data.datastore.AppSettingsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.SerializationException
import javax.inject.Inject

class SettingsUseCases @Inject constructor(
    private val dataStore: DataStore<AppSettings>
) {
    private val _state = MutableStateFlow<AppSettingsState>(AppSettingsState.Loading)
    val state = _state.asStateFlow()

    // loading in the data
    suspend fun loadSettingsData() {
        try {
            dataStore.data.collect {
                _state.value = AppSettingsState.Loaded(it)
            }
        }
        catch (error: SerializationException) {
            _state.value = AppSettingsState.Error(error.message)
        }
    }

    suspend fun updateDarkMode(darkMode: Boolean) {
        dataStore.updateData { settings: AppSettings ->
            settings.copy(darkMode = darkMode)
        }
    }

    // Update data
    suspend fun updatePlayImmediately(playImmediately: Boolean) {
        dataStore.updateData { settings: AppSettings ->
            settings.copy(playImmediately = playImmediately)
        }
    }

}