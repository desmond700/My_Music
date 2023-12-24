package com.example.mymusic.ui.screens.home

import SongsScreen
import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.EnterTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mymusic.R
import com.example.mymusic.model.Album
import com.example.mymusic.ui.components.AlbumListItem
import com.example.mymusic.ui.screens.album.AlbumSongsScreen
import com.example.mymusic.ui.screens.album.AlbumsScreen
import com.example.mymusic.ui.screens.artists.ArtistSongsScreen
import com.example.mymusic.ui.screens.artists.ArtistsScreen
import com.example.mymusic.ui.screens.player.PlayerScreen
import com.example.mymusic.ui.screens.playlists.PlaylistsScreen
import com.example.mymusic.ui.theme.MyMusicTheme
import com.example.mymusic.ui.viewmodels.HomeViewModel
import com.example.mymusic.ui.viewmodels.HomeViewModelState
import com.example.mymusic.ui.viewmodels.SongListViewModel
import com.example.mymusic.ui.viewmodels.SongViewModelState
import com.example.mymusic.utils.HomeRoutes
import com.example.mymusic.utils.HomeScreenSection
import com.example.mymusic.utils.HomeScreenSectionType
import kotlinx.coroutines.flow.StateFlow

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    songListViewModel: SongListViewModel = hiltViewModel()
) {
    // UiState of the HomeScreen that handle different states of home screen like Loading, Error,
    // Content etc.

    val songListUiState = songListViewModel.uiState
    val uiState by homeViewModel.uiState

    // Construct the lazy list state. This allows the associated state to survive beyond navigation
    // from and to the PdfViewerScreen or any other future screens, and therefore this way we get to
    // preserve the scroll throughout any changes to the content.
    val lazyListState = rememberLazyListState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val navController = rememberNavController()

//    Log.d("HomeScreen", "musicFiles count: ${uiState.musicFiles?.count()}")

//    uiState.musicFiles?.forEach {music ->
//        Log.d("HomeScreen", "---------------------------------")
//        Log.d("HomeScreen", "music Artist: ${music.getArtist()}")
//        Log.d("HomeScreen", "music Duration: ${music.getDuration()}")
//        Log.d("HomeScreen", "music Size: ${music.getSize()}")
//        Log.d("HomeScreen", "music songImageBitmap: ${music.songImageBitmap}")
//        Log.d("HomeScreen", "music albumArtist: ${music.albumArtist}")
//        Log.d("HomeScreen", "music title: ${music.title}")
//        Log.d("HomeScreen", "music genre: ${music.genre}")
//        Log.d("HomeScreen", "music writer: ${music.writer}")
//        Log.d("HomeScreen", "music trackName: ${music.title}")
//        Log.d("HomeScreen", "music composer: ${music.composer}")
//        Log.d("HomeScreen", "music year: ${music.year}")
//        Log.d("HomeScreen", "music modifiedAt: ${music.modifiedAt}")
//        Log.d("HomeScreen", "music path: ${music.path}")
//
//    }

//    Box(modifier = modifier) {
    PlayerScreen(
        modifier = modifier,
        progress = 0f
    ) { innerModifier ->
        NavHost(
            modifier = innerModifier.border(1.dp, Color.Blue),
            navController = navController,
            startDestination = HomeRoutes.Default.route,
            enterTransition = { EnterTransition.None }
        ) {
            composable(HomeRoutes.Default.route) {
                fun onNavigation(type: HomeScreenSectionType) {
                    when (type) {
                        HomeScreenSectionType.SONGS -> navController.navigate(HomeRoutes.Songs.route)
                        HomeScreenSectionType.ARTISTS -> navController.navigate(HomeRoutes.Artists.route)
                        HomeScreenSectionType.ALBUMS -> navController.navigate(HomeRoutes.Albums.route)
                        HomeScreenSectionType.PLAYLISTS -> navController.navigate(HomeRoutes.Playlists.route)
                    }
                }
                DefaultScreen(
                    uiState,
                    songListUiState,
                    onNavigation = { onNavigation(it) }
                )
            }
            composable(HomeRoutes.Songs.route) {
                SongsScreen(
                    onNavigation = { navController.popBackStack() },
                    viewModel = homeViewModel
                )
            }
            composable(HomeRoutes.Artists.route) {
                ArtistsScreen(
                    onNavigation = { navController.popBackStack() },
                    onNavigationArtistSongs = { artistId -> navController.navigate("artists/${artistId}") },
                    viewModel = homeViewModel
                )
            }
            composable(
                HomeRoutes.ArtistSongs.route,
                arguments = listOf(navArgument("artistId") {
                    type = NavType.StringType
                })
            ) { backStackEntry ->
                // Extract arguments from route
                val artistId = backStackEntry.arguments?.getString("artistId")

                artistId?.let { id ->
                    ArtistSongsScreen(
                        artistId = id,
                        onNavigation = {}
                    )
                }
            }
            composable(HomeRoutes.Albums.route) {
                AlbumsScreen(
                    onNavigation = { navController.popBackStack() },
                    onNavigationAlbumSongs = { albumId -> navController.navigate("albums/${albumId}") },
                    viewModel = homeViewModel
                )
            }
            composable(
                HomeRoutes.AlbumSongs.route,
                arguments = listOf(navArgument("albumId") {
                    type = NavType.StringType
                })
            ) { backStackEntry ->
                // Extract arguments from route
                val albumId = backStackEntry.arguments?.getString("albumId")

                albumId?.let { id ->
                    AlbumSongsScreen(
                        albumId = id,
                        onNavigation = { navController.popBackStack() }
                    )
                }

            }
            composable(HomeRoutes.Playlists.route) {
                PlaylistsScreen(
                    onNavigation = { navController.popBackStack() },
                )
            }

        }
    }
}

@Composable
fun DefaultScreen(
    uiState: HomeViewModelState,
    songListUiState: StateFlow<SongViewModelState>,
    onNavigation: (type: HomeScreenSectionType) -> Unit
) {
    val homeScreenSections = listOf(
        HomeScreenSection(
            label = "Songs",
            subLabel = songListUiState.value.songs.size,
            icon = R.drawable.music,
            type = HomeScreenSectionType.SONGS
        ),
        HomeScreenSection(
            label = "Artists",
            subLabel = uiState.artistsCount,
            icon = R.drawable.account_music,
            type = HomeScreenSectionType.ARTISTS
        ),
        HomeScreenSection(
            label = "Albums",
            subLabel = uiState.albumsCount,
            icon = R.drawable.album,
            type = HomeScreenSectionType.ALBUMS
        ),
        HomeScreenSection(
            label = "Playlists",
            subLabel = uiState.playlistsCount,
            icon = R.drawable.playlist_music,
            type = HomeScreenSectionType.PLAYLISTS
        )
    )
    // A surface container using the 'background' color from the theme
    Scaffold(
//            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = { HomeTopAppBar() }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(top = padding.calculateTopPadding())
        ) {
            item {
                Library(homeScreenSections, click = onNavigation)
            }
            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
            item {
//                YourActions()
                ActionItemList(title = "Recents")
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar() {
    CenterAlignedTopAppBar(
        modifier = Modifier.border(1.dp, Color.Yellow),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Red//MaterialTheme.colorScheme.background,
        ),
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    tint = Color.White,
                    contentDescription = null
                )
            }
        },
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleLarge
            )
        },
        actions = {
            IconButton(
                onClick = {

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

@Composable
fun HeaderLabel(text: String) {
    Text(
        modifier = Modifier.padding(bottom = 13.dp),
        text = text,
        style = MaterialTheme.typography.titleMedium
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Library(
    homeScreenSections: List<HomeScreenSection> = emptyList(),
    click: (type: HomeScreenSectionType) -> Unit
) {

    Column (
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .fillMaxWidth(),
    ) {
        HeaderLabel(text = "Library")
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            maxItemsInEachRow = 2
        ) {
            homeScreenSections.forEach {section ->
                LibraryItem(
                    label = section.label,
                    iconId = section.icon,
                    countText = section.subLabel,
                    click = {
                        click(section.type)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterialApi::class)
@Composable
fun FlowRowScope.LibraryItem(
    label: String,
    iconId: Int,
    countText: Int,
    click: () -> Unit
) {
//    fun String.toColor() = Color(android.graphics.Color.parseColor(this))
    val brush = Brush.linearGradient(
        colors = listOf(Color("#45475E".toColorInt()), Color("#353746".toColorInt())),
        start = Offset(0f, Float.POSITIVE_INFINITY),
        end = Offset(Float.POSITIVE_INFINITY, 0f)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(8.dp))
            .background(brush)
            .weight(1f)
            .aspectRatio(1f)
            .clickable { click() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier
                .height(42.dp)
                .width(42.dp)
                .padding(0.dp),
            painter = painterResource(id = iconId),
            tint = Color.White,
            contentDescription = null
        )
        Text(
            modifier = Modifier.padding(top = 5.dp),
            text = label,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier
                .widthIn(min = 25.dp)
                .background(Color.Gray, shape = RoundedCornerShape(10))
                .padding(horizontal = 3.dp, vertical = 3.dp),
            text = countText.toString(),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Composable
fun YourActions() {
    Column (
//        modifier = Modifier.padding(start = 20.dp, end = 20.dp)
    ) {
        HeaderLabel(text = "Your actions")
        ActionButtonItem()
        ActionButtonItem()
//        ActionButtonItem(showDivider = false)

    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ActionItemList(title: String) {
    val scrollState = rememberScrollState()

    Column {
        Box(Modifier.padding(start = 20.dp)) {
            Row(
                modifier = Modifier
                    .height(40.dp)
                    .clickable { },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null
                )
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        FlowRow(
            modifier = Modifier
                .horizontalScroll(state = scrollState),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Spacer(modifier = Modifier.width(5.dp))
            Array(11) {
                AlbumListItem(
                    modifier = Modifier
                        .height(120.dp)
                        ,
                    album = Album(
                        name = "Album Name",
                        artist = "Album Artist"
                    ),
                    click = {}
                )
//                Surface(
//                    modifier = Modifier
//                        .padding(0.dp)
//                        .height(120.dp)
//                        .aspectRatio(1f),
//                    shape = RoundedCornerShape(15.dp),
//                    color = Color.Gray
//                ) {
//
//                }
            }
            Spacer(modifier = Modifier.width(5.dp))
        }
    }
}

@Composable
fun ActionButtonItem(showDivider: Boolean = true) {
    Button(
        modifier = Modifier.padding(top = 5.dp, start = 0.dp),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        onClick = { /*TODO*/ }
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Icon (
                imageVector = Icons.Default.Favorite,
                tint = Color.Gray,
                contentDescription = null
            )
            Column(modifier = Modifier.padding(start = 20.dp)) {
                Row(modifier = Modifier.padding(bottom = 10.dp)) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "History",
                        style = TextStyle(
                            color = Color.White
                        )
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        tint = Color.Gray,
                        contentDescription = null
                    )
                }
                if (showDivider)
                    HorizontalDivider(
                        color = Color.DarkGray,
                        modifier = Modifier
                            .height(1.dp)
                            .fillMaxWidth()
                    )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun TopAppBarPreview() {
    MyMusicTheme {
        HomeTopAppBar()
    }
}

@Preview(showBackground = true)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun GridSectionsPreview() {
    MyMusicTheme {
        Library(click = {})
    }

}

@Preview(showBackground = true)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ActionButtonItemPreview() {
    MyMusicTheme {
        ActionButtonItem()
    }
}

@Preview(showBackground = true)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun YourActionsPreview() {
    MyMusicTheme {
        YourActions()
    }
}

@Preview()
@Composable
fun ActionItemListPreview() {
    MyMusicTheme {
        ActionItemList(title = "Recents")
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MyMusicTheme {
        val homeViewModel: HomeViewModel = viewModel()
        HomeScreen(homeViewModel = homeViewModel)
    }

}