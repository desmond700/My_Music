package com.example.mymusic.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.mymusic.data.repository.playlistRepository
import com.example.mymusic.usecases.MusicUseCases
import javax.inject.Inject

class PlaylistViewModel @Inject constructor(
    private val musicUseCases: MusicUseCases,
    private val playlistRepository: playlistRepository
): ViewModel() {

}