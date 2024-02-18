package com.example.mymusic.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.data.repository.ContentResolverRepository
import com.example.mymusic.data.repository.SongRepository
import com.example.mymusic.data.room.entities.Song
import com.example.mymusic.usecases.MusicUseCases
import com.example.mymusic.usecases.MusicUseCasesLibraryItem
import com.example.mymusic.utils.ErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SongViewModelState (
    val songs: List<Song> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList()
)

class ViewModelState<T> (
    list: List<T> = emptyList(),
    searchQuery: String = "",
    isLoading: Boolean = false,
    errorMessages: List<ErrorMessage> = emptyList()
) {
    val songs = list
    val searchQuery = searchQuery
    val isLoading = isLoading
    val errorMessages = errorMessages
}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SongListViewModel @Inject constructor(
    private val musicUseCases: MusicUseCases,
    private val contentResolverRepository: ContentResolverRepository,
    private val songRepository: SongRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel() {
    private val _uiState = MutableStateFlow(SongViewModelState())
    val uiState = _uiState.asStateFlow()

    val searchQuery = MutableStateFlow("")
    val searchedDataList = searchQuery.flatMapLatest { query ->
        searchMusicFiles(query)
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            songRepository.getAllSongs().collectLatest { songs ->
                _uiState.update {
                    it.copy(
                        songs = songs,
                        isLoading = false
                    )
                }
            }
        }

        loadData()
    }

    private fun searchMusicFiles(query: String): Flow<List<Song>> = flow {
        _uiState.value.songs
            .filter { music -> music.artistName.contains(query) || music.title.contains(query) }
            .let { musicList -> emit(musicList) }
    }

    fun refreshData() {
        loadData()
    }

    private fun loadData() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            contentResolverRepository.loadAllAudio()
        }
    }

    fun addToFavourite(songId: Long, isFavourite: Boolean) {
        songRepository.updateFavourite(songId, isFavourite)
    }

    fun selectedMusicItem(audioList: List<Song>, songId: Long, audioIndex: Int) {
        musicUseCases.selectAudioItem(
            audioList = audioList,
            audioIndex = audioIndex,
            libraryItem = MusicUseCasesLibraryItem.Song
        )
    }

}