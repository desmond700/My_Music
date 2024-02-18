package com.example.mymusic.ui.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.data.repository.PlaylistRepository
import com.example.mymusic.data.room.entities.Playlist
import com.example.mymusic.data.room.entities.PlaylistSongCrossRef
import com.example.mymusic.data.room.relationship.PlaylistWithSongs
import com.example.mymusic.usecases.MusicUseCases
import com.example.mymusic.utils.ErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlaylistViewModelState(
    val playlists: List<PlaylistWithSongs> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList()
) {
    fun getPlaylist(playlistId: Long): PlaylistWithSongs? {
        return playlists.find { it.playlist.playlistId == playlistId }
    }
}

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val musicUseCases: MusicUseCases,
    private val playlistRepository: PlaylistRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
    savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val _uiState = MutableStateFlow(PlaylistViewModelState())
    val uiState = _uiState.asStateFlow()

    val songId: Long? = savedStateHandle["songId"]

    init {
        viewModelScope.launch(coroutineDispatcher) {
            playlistRepository.getPlaylists().collectLatest { playlists ->
                _uiState.update { it.copy(playlists = playlists) }
            }
        }
    }

    fun createPlaylist(playlistName: String) {
        viewModelScope.launch(coroutineDispatcher) {
            val playlist = Playlist(playlistName = playlistName)
            playlistRepository.insertPlaylist(playlist)
        }
    }

    fun addToPlaylists(playlistIds: Set<Long>) {
        if (songId != null) {
            val playlistSongs = playlistIds.map { playlistId ->
                PlaylistSongCrossRef(playlistId, songId)
            }

            Log.d("PlaylistViewModel", "addToPlaylists playlistSongs $playlistSongs")

//        playlistRepository.insertPlaylistSongs(playlistSongs)
        }
    }

    fun updatePlaylist(playlist: Playlist) = playlistRepository.updatePlaylist(playlist)

    fun deletePlaylist(playlist: Playlist) = playlistRepository.deletePlaylist(playlist)

}