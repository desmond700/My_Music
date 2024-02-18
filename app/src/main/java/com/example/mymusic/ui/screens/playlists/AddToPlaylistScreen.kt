package com.example.mymusic.ui.screens.playlists

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mymusic.data.room.relationship.PlaylistWithSongs
import com.example.mymusic.ui.components.BaseListRowItem
import com.example.mymusic.ui.components.MusicArt
import com.example.mymusic.ui.components.SearchAppBar
import com.example.mymusic.ui.components.SubTitleText
import com.example.mymusic.ui.components.TitleText
import com.example.mymusic.ui.viewmodels.PlaylistViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToPlaylistScreen(
    navController: NavHostController,
    viewModel: PlaylistViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val openAlertDialog = remember { mutableStateOf(false) }

    when {
        openAlertDialog.value -> CreatePlaylistDialog(
            onDismissRequest = { openAlertDialog.value = false },
            onConfirmation = { value ->
                openAlertDialog.value = false
                println("Confirmation registered") // Add logic here to handle confirmation.
                viewModel.createPlaylist(playlistName = value)
            },
        )
    }

    Scaffold(
        topBar = {
            SearchAppBar(
                label = "Add to Playlist",
                onNavigation = { navController.popBackStack() },
                placeholderText = "Search for Playlist",
                onValueChange = { text ->
                    Log.d("SongsScreen", "onValueChange $text")

//                    viewModel.searchQuery.value = text
                },
                onClose = {}
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            CreateNewPlaylistButton(
                click = { openAlertDialog.value = true }
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 5.dp),
                color = Color.DarkGray
            )
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.playlists.isEmpty()) {
                    NoPlaylistAvailable()
                }
                else {
                    AddToPlaylistContent(
                        playlists = uiState.playlists,
                        onAddToPlaylist = { playlistIds ->
                            viewModel.addToPlaylists(playlistIds)
                        }
                    )
                }
            }
        }

    }
}

@Composable
fun NoPlaylistAvailable() {
    Text(
        text = "No Playlist Available.",
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
fun AddToPlaylistContent(
    playlists: List<PlaylistWithSongs>,
    onAddToPlaylist: (playlistIds: Set<Long>) -> Unit
) {
    var selectedPlaylists by remember { mutableStateOf(emptySet<Long>()) }
    val playlistText = if (selectedPlaylists.size == 1) "playlist" else "playlists"

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
//            .background(Color.Blue)
    ) {
        val (listItem, addButton) = createRefs()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
//                .background(Color.Red)
                .padding(0.dp)
                .constrainAs(listItem) {
                    height = Dimension.fillToConstraints
                    top.linkTo(parent.top, 0.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(addButton.top)
                }
        ) {
            items(playlists) { playlistWithSongs ->
                AddToPlaylistItem(
                    playlistWithSongs = playlistWithSongs,
                    onCheckedChange = { isChecked ->
                        val (playlist) = playlistWithSongs
                        Log.d("AddToPlaylistItem", "isChecked $isChecked")

                        selectedPlaylists = if (isChecked) {
                            selectedPlaylists.plus(playlist.playlistId)
                        } else {
                            selectedPlaylists.minus(playlist.playlistId)
                        }

                        Log.d("AddToPlaylistItem", "selectedPlaylists $selectedPlaylists")
                    }
                )
            }
        }
        Column(
            modifier = Modifier
                .constrainAs(addButton) {
                    bottom.linkTo(parent.bottom)
                    centerHorizontallyTo(parent)
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${selectedPlaylists.size} $playlistText selected",
                style = MaterialTheme.typography.titleMedium
            )
            AddToPlaylistButton(
                click = { onAddToPlaylist(selectedPlaylists) }
            )
        }
    }
}

@Composable
fun AddToPlaylistItem(
    playlistWithSongs: PlaylistWithSongs,
    onCheckedChange: (isChecked: Boolean) -> Unit
) {
    val (playlist, songs) = playlistWithSongs
    val songText = if (songs.size == 1) "song" else "songs"

    var checkedState by remember { mutableStateOf(false) }

    BaseListRowItem(
        onTap = {
            checkedState = checkedState.not()
        },
        horizontalSpacing = 10.dp,
        verticalSpacing = 10.dp,
        leading = {
           MusicArt(
               modifier = Modifier.size(60.dp),
               bitmap = null
           )
        },
        title = {
            TitleText(text = playlist.playlistName)
        },
        subTitle = {
            SubTitleText(text = "${songs.size} $songText")
        },
        trailing = {
            Checkbox(
                checked = checkedState,
                onCheckedChange = {
                    checkedState = it
                    onCheckedChange(it)
                }
            )
        }
    )
}

@Composable
fun AddToPlaylistButton(
    modifier: Modifier = Modifier,
    click: () -> Unit
) {
    OutlinedButton(
        modifier = modifier.padding(vertical = 6.dp),
        onClick = click
    ) {
        Row {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add to playlist button"
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = "Add to Playlist",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}