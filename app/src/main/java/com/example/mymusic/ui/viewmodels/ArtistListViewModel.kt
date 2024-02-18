package com.example.mymusic.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.data.repository.ArtistRepository
import com.example.mymusic.data.room.relationship.ArtistWithSongs
import com.example.mymusic.usecases.MusicUseCases
import com.example.mymusic.utils.ErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ArtistListViewModelState(
    val artists: List<ArtistWithSongs> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList()
) {
    fun getArtist(artistId: Long): ArtistWithSongs? {
        return artists.find { it.artist.artistId == artistId }
    }
}

@HiltViewModel
class ArtistListViewModel @Inject constructor(
    private val musicUseCases: MusicUseCases,
    private val artistRepository: ArtistRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(ArtistListViewModelState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            artistRepository.getArtistsWithSongs().collectLatest { artists ->
                _uiState.update { it.copy(artists = artists) }
            }
        }
    }

    fun refreshData() {
        loadData()
    }

    fun loadData() {

    }
}