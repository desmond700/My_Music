package com.example.mymusic.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlaylistDetailsViewModel @Inject constructor(

    savedStateHandle: SavedStateHandle,
): ViewModel() {

    val playlistId: Long? = savedStateHandle["playlistId"]
}