package com.example.mymusic.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.mymusic.data.repository.AlbumRepository
import com.example.mymusic.usecases.MusicUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val musicUseCases: MusicUseCases,
    private val albumRepository: AlbumRepository
): ViewModel() {

}