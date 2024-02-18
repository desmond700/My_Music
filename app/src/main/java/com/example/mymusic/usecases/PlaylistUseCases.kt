package com.example.mymusic.usecases

import com.example.mymusic.data.repository.PlaylistRepository
import com.example.mymusic.data.room.relationship.PlaylistWithSongs
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

class PlaylistUseCases @Inject constructor(
    private val playlistRepository: PlaylistRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private var scope = CoroutineScope(ioDispatcher)
    private val _playlistWithSongs = MutableStateFlow(emptyList<PlaylistWithSongs>())
    private val playlistWithSongs = _playlistWithSongs.asStateFlow()

    init {
        scope.launch {
            playlistRepository.getPlaylists().collectLatest { playlists ->
                _playlistWithSongs.update { playlists }
            }
        }
    }

    suspend fun getPlaylistWithSongsByPlaylistId(playlistId: Long): PlaylistWithSongs? {
        return when (playlistWithSongs.value.isNotEmpty()) {
            true -> {
                playlistWithSongs.value.find { it.playlist.playlistId == playlistId }
            }
            else -> {
                playlistRepository.getPlaylistWithSongsByPlaylistId(playlistId).last()
            }
        }
    }
}