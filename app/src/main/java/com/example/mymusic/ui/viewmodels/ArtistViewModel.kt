package com.example.mymusic.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.mymusic.data.repository.ArtistRepository
import com.example.mymusic.usecases.MusicUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val musicUseCases: MusicUseCases,
    private val artistRepository: ArtistRepository
): ViewModel() {

}