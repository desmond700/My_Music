package com.example.mymusic.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.data.repository.ArtistRepository
import com.example.mymusic.data.room.entities.Song
import com.example.mymusic.data.room.relationship.ArtistWithSongs
import com.example.mymusic.usecases.MusicUseCases
import com.example.mymusic.usecases.MusicUseCasesLibraryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistDetailsViewModel @Inject constructor(
    private val musicUseCases: MusicUseCases,
    private val artistRepository: ArtistRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val artistId: Long? = savedStateHandle["artistId"]

    val artistWithSongs = channelFlow {
        if (artistId != null) {
            artistRepository.getArtistWithSongsByArtistId(artistId).collectLatest { artistWithSongs ->
                send(artistWithSongs)
            }
        }
        else {
            send(ArtistWithSongs.default)
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, ArtistWithSongs.default)

    fun play(audioList: List<Song>, albumId: Long, audioIndex: Int? = null) {
        musicUseCases.selectAudioItem(
            audioList = audioList,
            audioIndex = audioIndex ?: 0,
            libraryItem = MusicUseCasesLibraryItem.Album(albumId)
        )
    }

    fun shuffleAlbumList() {
        viewModelScope.launch {
            musicUseCases.onPlayerUIEvents(UIEvents.Shuffle)
        }
    }
}