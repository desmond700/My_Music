package com.example.mymusic.usecases

import com.example.mymusic.data.repository.AlbumRepository
import com.example.mymusic.data.room.relationship.AlbumWithSongs
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class AlbumUseCases @Inject constructor(
    private val albumRepository: AlbumRepository,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val scope = CoroutineScope(coroutineDispatcher)
    private val _albumsWithSongs = MutableStateFlow(emptyList<AlbumWithSongs>())
    val albumWithSongs = _albumsWithSongs.asStateFlow()

    init {
        scope.launch {
            albumRepository.getAlbumsWithSongs().collectLatest { albumsWithSongs ->
                _albumsWithSongs.update { albumsWithSongs }
            }
        }
    }

    suspend fun getAlbumWithSongsByAlbumId(albumId: Long): AlbumWithSongs {
        return when (albumWithSongs.value.isNotEmpty()) {
            true -> {
                albumWithSongs.value.find { it.album.albumId == albumId } ?: AlbumWithSongs.default
            }
            else -> {
                albumRepository.getAlbumWithSongsByAlbumId(albumId).last()
            }
        }
    }
}