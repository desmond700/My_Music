package com.example.mymusic.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusic.R
import com.example.mymusic.data.Result
import com.example.mymusic.data.repository.ContentResolverRepository
import com.example.mymusic.data.repository.SongRepository
import com.example.mymusic.data.room.entities.Song
import com.example.mymusic.data.successOr
import com.example.mymusic.usecases.MusicUseCases
import com.example.mymusic.utils.ErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class SongViewModelState (
    val songs: List<Song> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList()
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SongListViewModel @Inject constructor(
    private val musicUseCases: MusicUseCases,
    private val contentResolverRepository: ContentResolverRepository,
    private val songRepository: SongRepository,
//    private val coroutineScope: CoroutineScope,
    private val IODispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel() {

    private val _uiState = MutableStateFlow(SongViewModelState())
    val uiState = _uiState.stateIn(viewModelScope, SharingStarted.Lazily, SongViewModelState())

    val searchQuery = MutableStateFlow("")

    val searchedDataList = searchQuery.flatMapLatest { query ->
        searchMusicFiles(query)
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
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

        viewModelScope.launch(IODispatcher) {
            val resultDeferred = async { contentResolverRepository.getAllAudio() }

            when(val result = resultDeferred.await()) {
                is Result.Success -> {
                    val songs = result.successOr(emptyList())
//                    coroutineScope.launch(IODispatcher) {
                        songRepository.insertAllSongs(songs)
                        songRepository.getAllSongs().collectLatest { songList ->
                            _uiState.update { it.copy(
                                songs = songList,
                                isLoading = false
                            ) }
//                        }
                    }

                }
                is Result.Error -> {
                    _uiState.collectLatest { latestItem ->
                        val errorMessages = latestItem.errorMessages + ErrorMessage(
                            id = UUID.randomUUID().mostSignificantBits,
                            messageId = R.string.load_error
                        )

                        _uiState.update { updateItem ->
                            updateItem.copy(
                                errorMessages = errorMessages,
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }
}