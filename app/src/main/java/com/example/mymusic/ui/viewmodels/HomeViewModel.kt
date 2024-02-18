package com.example.mymusic.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.data.repository.AlbumRepository
import com.example.mymusic.data.repository.ArtistRepository
import com.example.mymusic.data.repository.MusicPlayedAmountRepository
import com.example.mymusic.data.repository.PlaylistRepository
import com.example.mymusic.data.repository.SongRepository
import com.example.mymusic.utils.HomeScreenAction
import com.example.mymusic.utils.Library
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val actionList: List<HomeScreenAction>? = null,
    val libraryItems: List<Library> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val musicPlayedAmountRepository: MusicPlayedAmountRepository,
    private val songRepository: SongRepository,
    private val albumRepository: AlbumRepository,
    private val artistRepository: ArtistRepository,
    private val playlistRepository: PlaylistRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val recentlyPlayed = musicPlayedAmountRepository.getRecentlyPlayed()
            val mostPlayed = musicPlayedAmountRepository.getMostPlayed()
            Log.d(TAG, "init")

            recentlyPlayed.combine(mostPlayed) { recentsList, mostPlayedList ->
                Log.d(TAG, "recentsList $recentsList")
                Log.d(TAG, "mostPlayedList $mostPlayedList")
                _uiState.update {
                    it.copy(
                        actionList = listOf(
                            HomeScreenAction(
                                title = "Recents",
                                list = recentsList
                            ),
                            HomeScreenAction(
                                title = "Most Played",
                                list = mostPlayedList
                            )
                        )
                    )
                }
            }.collect()
        }
    }

    init {
        val songsCountFlow = songRepository.getAllSongCount()
        val albumCountFlow = albumRepository.getAlbumsCount()
        val artistCountFlow = artistRepository.getArtistsCount()
        val playlistsCountFlow = playlistRepository.getPlaylistsCount()

        combine(
            songsCountFlow,
            albumCountFlow,
            artistCountFlow,
            playlistsCountFlow
        ) { songsCount, albumCount, artistCount, playlistsCount ->
            val libraries: List<Library> = listOf(
                Library(
                    name = "Songs",
                    icon = 0,
                    contentSize = songsCount
                ),
                Library(
                    name = "Artists",
                    icon = 0,
                    contentSize = artistCount
                ),
                Library(
                    name = "Albums",
                    icon = 0,
                    contentSize = albumCount
                ),
                Library(
                    name = "Playlists",
                    icon = 0,
                    contentSize = playlistsCount
                )
            )

            _uiState.update { it.copy(libraryItems = libraries) }
        }
    }

    companion object {
        const val TAG = "HomeViewModel"
    }
}