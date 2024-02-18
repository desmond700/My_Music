package com.example.mymusic.ui.screens.playlists

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.mymusic.R
import com.example.mymusic.data.room.entities.Playlist
import com.example.mymusic.data.room.relationship.PlaylistWithSongs
import com.example.mymusic.ui.components.BaseListRowItem
import com.example.mymusic.ui.components.MenuButton
import com.example.mymusic.ui.components.MusicArt
import com.example.mymusic.ui.components.SubTitleText
import com.example.mymusic.ui.components.TitleText
import com.example.mymusic.ui.screens.playlists.ui.theme.MyMusicTheme
import com.example.mymusic.ui.viewmodels.PlaylistViewModel
import com.example.mymusic.utils.quantityAware

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistsScreen(
    navController: NavHostController,
    viewModel: PlaylistViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val openAlertDialog = remember { mutableStateOf(false) }
    var selectModeEnabled by remember { mutableStateOf(false) }
    val selectAllItems by remember { mutableStateOf(false) }

    when {
        openAlertDialog.value -> createPlaylistDialog(
                onDismissRequest = { openAlertDialog.value = false },
                onConfirmation = { value ->
                    openAlertDialog.value = false
                    println("Confirmation registered") // Add logic here to handle confirmation.
                    viewModel.createPlaylist(playlistName = value)
                },
            )
    }

    MyMusicTheme {
        // A surface container using the 'background' color from the theme
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    title = {
                        Text(
                            text = "Playlists",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    actions = {
                        if (selectModeEnabled) {
                            IconButton(onClick = {  }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.check_all),
                                    contentDescription = "Search icon"
                                )
                            }
                        }
                        else {
                            IconButton(onClick = {  }) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search icon"
                                )
                            }
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.playlists.isEmpty()) {
                    NoPlaylistsAvailable(openAlertDialog)
                } else {
                    PlaylistContent(
                        selectAllItems = selectAllItems,
                        selectModeEnabled = selectModeEnabled,
                        openAlertDialog = openAlertDialog,
                        playlists = uiState.playlists,
                        selectedListItem = { playlistId ->
                            navController.navigate("playlists/$playlistId")
                        },
                        toggleSelectMode = { selectModeEnabled = true }
                    )
                }
            }
        }
    }
}

@Preview()
@Composable
fun NoPlaylistsAvailable(
    openAlertDialog: MutableState<Boolean> = mutableStateOf(false)
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "No Playlist Available")
        Spacer(modifier = Modifier.height(5.dp))
        Button(onClick = { openAlertDialog.value = true }) {
            Text(text = "Create Playlist")
        }
    }
}

@Preview()
@Composable
fun PlaylistContent(
    selectAllItems: Boolean = false,
    openAlertDialog: MutableState<Boolean> = mutableStateOf(false),
    selectModeEnabled: Boolean = false,
    playlists: List<PlaylistWithSongs> = emptyList(),
    selectedListItem: (Long) -> Unit = {},
    toggleSelectMode: () -> Unit = {}
) {

    var selectedPlaylists by remember { mutableStateOf<Set<Playlist>>(emptySet()) }
    val playlistText = "playlists".quantityAware(selectedPlaylists.size)
    val playlistTextAlpha = if (selectModeEnabled) 1f else 0f

    Column(modifier = Modifier.fillMaxSize()) {
        if (selectModeEnabled) {
            SelectedPlaylistCount(selected = selectedPlaylists.size)
        }
        else {
            CreateNewPlaylistButton(click = {})
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        ) {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 5.dp),
                color = Color.DarkGray
            )
        }
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (list, playlistCountText) = createRefs()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(list) {
                        height = Dimension.fillToConstraints
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(playlistCountText.top)
                    }
            ) {
                items(playlists) { playlistWithSongs ->
                    PlaylistListItem(
                        selectModeEnabled = selectModeEnabled,
                        playlistWithSongs = playlistWithSongs,
                        onClick = { selectedListItem(playlistWithSongs.playlist.playlistId) },
                        onLongPress = toggleSelectMode,
                        onCheckedChange = { isChecked ->
                            val (playlist) = playlistWithSongs

                            selectedPlaylists = if (isChecked) {
                                selectedPlaylists.plus(playlist)
                            } else {
                                selectedPlaylists.minus(playlist)
                            }
                        }
                    )
                }
            }

            Text(
                modifier = Modifier
                    .alpha(playlistTextAlpha)
                    .padding(vertical = 8.dp)
                    .constrainAs(playlistCountText) {
                        bottom.linkTo(parent.bottom)
                        centerHorizontallyTo(parent)
                    },
                text = "${selectedPlaylists.size} $playlistText selected",
                style = MaterialTheme.typography.titleMedium
            )

        }
    }
}

@Composable
fun PlaylistListItem(
    selectModeEnabled: Boolean,
    playlistWithSongs: PlaylistWithSongs,
    onClick: () -> Unit,
    onLongPress: () -> Unit,
    onCheckedChange: (isChecked: Boolean) -> Unit
) {
    val (playlist, songs) = playlistWithSongs
    val songText = if(songs.size > 1) "songs" else "song"
    var checkedState by remember { mutableStateOf(false) }

    BaseListRowItem(
        onTap = { onClick() },
        onLongPress = {
            if (selectModeEnabled.not()) {
                onLongPress()
            }
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
            if (selectModeEnabled) {
                Checkbox(
                    checked = checkedState,
                    onCheckedChange = {
                        checkedState = it
                        onCheckedChange(it)
                    }
                )
            }
            else {
                MenuButton(
                    click = {  },
                    items = listOf(

                    )
                )
            }
        }
    )
}

@Composable
fun Greeting6(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Preview(showBackground = true)
//@Composable
//fun PlaylistsScreenPreview6() {
//    MyMusicTheme {
//        PlaylistsScreen(onNavigation = {})
//    }
//}