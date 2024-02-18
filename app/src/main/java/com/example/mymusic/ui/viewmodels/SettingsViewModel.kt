package com.example.mymusic.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.data.datastore.AppSettings
import com.example.mymusic.data.datastore.AppSettingsState
import com.example.mymusic.usecases.SettingsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsUseCases: SettingsUseCases
): ViewModel() {

    private val _settingsState = MutableStateFlow(AppSettings.default)
    val settingsState = _settingsState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsUseCases.state.collect { appSettings ->
                // listening to changes in the settings
                // convert it to other state and create representation in the UI
                when(appSettings) {
                    is AppSettingsState.Loading -> {

                    }
                    is AppSettingsState.Loaded -> {
                        Log.d(TAG, "loaded: ${appSettings.data}")
                        _settingsState.update { appSettings.data }
                    }
                    is AppSettingsState.Error -> {
                        Log.d(TAG, "error: ${appSettings.message}")
                    }
                }
            }
        }

        viewModelScope.launch {
            // initial loading in the data
            settingsUseCases.loadSettingsData()
        }
    }

    fun setDarkMode(darkMode: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsUseCases.updateDarkMode(darkMode)
        }
    }

    fun playSongImmediately(playImmediately: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsUseCases.updatePlayImmediately(playImmediately)
        }
    }

    companion object {
        val TAG = "SettingsViewModel"
    }
}