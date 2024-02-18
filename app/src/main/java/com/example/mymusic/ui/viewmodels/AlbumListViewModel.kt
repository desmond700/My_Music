package com.example.mymusic.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.data.repository.AlbumRepository
import com.example.mymusic.data.repository.ContentResolverRepository
import com.example.mymusic.data.room.entities.Album
import com.example.mymusic.data.room.relationship.AlbumWithSongs
import com.example.mymusic.usecases.AlbumUseCases
import com.example.mymusic.usecases.MusicUseCases
import com.example.mymusic.utils.ErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class AlbumListViewModelState(
    val albums: List<AlbumWithSongs> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList()
) {
    fun getAlbum(albumId: Long): AlbumWithSongs? {
        return albums.find { it.album.albumId == albumId }
    }
}

@HiltViewModel
class AlbumListViewModel @Inject constructor(
    private val musicUseCases: MusicUseCases,
    private val contentResolverRepository: ContentResolverRepository,
    private val albumUseCases: AlbumUseCases,
    private val albumRepository: AlbumRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(AlbumListViewModelState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            albumUseCases.albumWithSongs.collectLatest { albumsWithSongs ->
                _uiState.update {
                    it.copy(
                        albums = albumsWithSongs,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun refreshAlbums() {

    }

    fun updateAlbum(album: Album) = albumRepository.updateAlbum(album)

    fun deleteAlbum(album: Album) = albumRepository.deleteAlbum(album)
}