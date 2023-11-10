package com.example.mymusic.ui.viewmodels

import android.os.Environment
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mymusic.R
import com.example.mymusic.data.Result
import com.example.mymusic.data.file.IFileRepository
import com.example.mymusic.model.MusicFile
import com.example.mymusic.utils.ErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
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


sealed interface HomeUiState {
    val isLoading: Boolean
    val errorMessages: List<ErrorMessage>
    val searchInput: String


    data class NoFiles(
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>,
        override val searchInput: String
    ) : HomeUiState


    data class HasFiles(
        val filesList: List<MusicFile>,
        val favourites: Set<String>,
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>,
        override val searchInput: String,
    ) : HomeUiState
}

data class HomeViewModelState(
    val musicFiles: List<MusicFile>? = null,
    val favourites: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
    var searchInput: String = ""
) {

    fun toUiState(): HomeUiState = if (musicFiles == null) {
        HomeUiState.NoFiles(
            isLoading = isLoading,
            errorMessages = errorMessages,
            searchInput = searchInput
        )
    }
    else {
        HomeUiState.HasFiles(
            filesList = musicFiles,
            favourites = favourites,
            isLoading = isLoading,
            errorMessages = errorMessages,
            searchInput = searchInput
        )
    }
}
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
        private val fileRepository: IFileRepository
    ): ViewModel(), DefaultLifecycleObserver {

    private val viewModelState = MutableStateFlow(HomeViewModelState(isLoading = true))

    private val _uiState = mutableStateOf(HomeViewModelState())
    val uiState: State<HomeViewModelState> = _uiState

    // UI state exposed to the screens
//    val uiState = viewModelState
//        .map { it.toUiState() }
//        .stateIn(
//            viewModelScope,
//            SharingStarted.Lazily,
//            viewModelState.value.toUiState()
//        )

    var searchQuery = MutableStateFlow("")
    val searchedMusicList = searchQuery.flatMapLatest { query ->
        searchMusicFiles(query)
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        refreshFiles()
    }

    private fun searchMusicFiles(query: String): Flow<List<MusicFile>> = flow {
        Log.d("HomeViewModel", "searchMusicFiles query $query")
        Log.d("HomeViewModel", "searchMusicFiles viewModelState.value.musicFiles count ${viewModelState.value.musicFiles?.count()}")
        Log.d("HomeViewModel", "searchMusicFiles viewModelState.value.musicFiles count ${_uiState.value.musicFiles?.count()}")
        _uiState.value.musicFiles
                ?.filter { music -> music.name.contains(query) }
                ?.let {musicList ->
                    Log.d("HomeViewModel", "searchMusicFiles count ${musicList.count()}")

                    emit(musicList)
                }

    }

    // Refresh files and update the UI state accordingly
    fun refreshFiles() {
        Log.d("HomeViewModel", "refreshFiles")
        // Ui state is refreshing
        viewModelState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            // Provide the data source of the files. For the demo purpose, I've fetched the file from
            // Downloads directory. You can change to whatever you want
            val result = fileRepository.getFiles(downloadsDirectory)

            viewModelState.collectLatest {
                when(result) {
                    is Result.Success -> {
                        Log.d("HomeViewModel", "refreshFiles result success count ${result.data.count()}")

                        // Error the file names against the searchInput
                        val filteredFiles = result.data.filter { file ->
                            file.name.contains(viewModelState.value.searchInput, ignoreCase = true)
                        }

//                        it.copy(musicFiles = filteredFiles, isLoading = false)

                        _uiState.value = uiState.value.copy(musicFiles = filteredFiles, isLoading = false)
                    }
                    is Result.Error -> {
                        Log.d("HomeViewModel", "refreshFiles result error ${it.errorMessages}")

                        val errorMessages = it.errorMessages + ErrorMessage(
                            id = UUID.randomUUID().mostSignificantBits,
                            messageId = R.string.load_error
                        )
//                        it.copy(errorMessages = errorMessages, isLoading = false)

                        _uiState.value = uiState.value.copy(errorMessages = errorMessages, isLoading = false)
                    }
                }
            }
        }
    }

    fun toggleFavourite(fileUri: String) {
        viewModelScope.launch {
            fileRepository.toggleFavourite(fileUri)
        }
    }

    /**
     * Notify that an error was displayed on the screen
     */
    fun errorShown(errorId: Long) {
        viewModelState.update { currentUiState ->
            val errorMessages = currentUiState.errorMessages.filterNot{ it.id == errorId }
            currentUiState.copy(errorMessages = errorMessages)
        }
    }

    /**
     * Notify that the user update the search query
     */
    fun onSearchInputChanged(searchInput: String) {
        viewModelState.update {
            it.copy(searchInput = searchInput)
        }
        refreshFiles()
    }

    /**
     * Factory for HomeViewModel that takes [IFileRepository] as a dependency
     */
    companion object {
        private val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        fun provideFactory(fileRepository: IFileRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {

                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HomeViewModel(fileRepository) as T
                }
            }
    }
}