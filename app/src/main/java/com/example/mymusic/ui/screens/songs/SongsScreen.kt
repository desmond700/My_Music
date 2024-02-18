

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.mymusic.data.room.entities.Song
import com.example.mymusic.ui.components.LoadingContent
import com.example.mymusic.ui.components.MusicListItem
import com.example.mymusic.ui.components.SearchBar
import com.example.mymusic.ui.viewmodels.SongListViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongsScreen(
    navController: NavHostController,
    onNavigation: () -> Unit,
    viewModel: SongListViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val songList by viewModel.searchedDataList.collectAsStateWithLifecycle()

    Log.d("SongsScreen", "songList count ${songList.count()}")

    // A surface container using the 'background' color from the theme
//    Surface(
//        modifier = Modifier.fillMaxSize(),
//        color = MaterialTheme.colorScheme.background,
//    ) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var showBottomSheet by remember { mutableStateOf(false) }


    val scope = rememberCoroutineScope()

    val searchState = remember { mutableStateOf(false) }

//    ModalBottomSheet(onDismissRequest = {  }) {
//
//    }
    val SEARCHBAR_HEIGHT = 60.dp
    val statusBarTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .zIndex(1f),
                navigationIcon = {
                    IconButton(
                        onClick = onNavigation
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            tint = Color.White,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text(
                        text = "Songs",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            showBottomSheet = true
                            scope.launch {
                                sheetState.show()
                            }
                            searchState.value = true

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            tint = Color.White,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->

        SongsScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            isLoading = uiState.isLoading,
            songList = songList,
            refreshFiles = { viewModel.refreshData() },
            onListItemSelected = { index, songId ->
                Log.d("SongsScreen", "SongsScreenContent onClick index $index")
                viewModel.selectedMusicItem(songList, songId, index)
            },
            onAddPlaylist = { songId ->
                navController.navigate("addToPlaylist/${songId}")
            },
            onAddToFavourites = { songId, isFavourite ->
                viewModel.addToFavourite(songId, isFavourite)
            }
        )
    }

//        SearchContainer(
//            state = searchState,
//            maxHeight = maxHeight
//        )

    if (showBottomSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { showBottomSheet = false }
        ) {
            SearchBar(
                placeholderText = "Search for Songs",
                onClose = {  }
            )
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No Search Results")
            }
        }
    }



}

@Composable
fun SongsScreenContent(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    songList: List<Song>,
    refreshFiles: () -> Unit,
    onListItemSelected: (index: Int, songId: Long) -> Unit,
    onAddPlaylist: (songId: Long) -> Unit,
    onAddToFavourites: (songId: Long, isFavourite: Boolean) -> Unit
) {
    LoadingContent(
        empty = songList.isEmpty(),
        loading = isLoading,
        onRefresh = { refreshFiles() }
    ) {
        LazyColumn(modifier = modifier) {
            itemsIndexed(songList) { index, song ->
                MusicListItem(
                    song = song,
                    click = { onListItemSelected(index, song.songId) },
                    onAddToPlaylist = { onAddPlaylist(song.songId) },
                    onAddToFavourites = { onAddToFavourites(song.songId, song.favourite.not()) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SongsScreenContentPreview() {
    SongsScreenContent(
        isLoading = false,
        songList = emptyList(),
        refreshFiles = {},
        onListItemSelected = {_, _ ->},
        onAddPlaylist = {},
        onAddToFavourites = { _, _ -> }
    )
}