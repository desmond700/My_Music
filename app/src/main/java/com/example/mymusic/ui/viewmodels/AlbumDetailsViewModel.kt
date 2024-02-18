package com.example.mymusic.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.data.room.entities.Song
import com.example.mymusic.data.room.relationship.AlbumWithSongs
import com.example.mymusic.usecases.AlbumUseCases
import com.example.mymusic.usecases.MusicUseCases
import com.example.mymusic.usecases.MusicUseCasesLibraryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumDetailsViewModel @Inject constructor(
    private val musicUseCases: MusicUseCases,
    private val albumUseCases: AlbumUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val albumId: Long = checkNotNull(savedStateHandle["albumId"])

    val albumWithSongs = flow {
        val albumWithSongs = albumUseCases.getAlbumWithSongsByAlbumId(albumId)
        emit(albumWithSongs)
    }.stateIn(viewModelScope, SharingStarted.Lazily, AlbumWithSongs.default)

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