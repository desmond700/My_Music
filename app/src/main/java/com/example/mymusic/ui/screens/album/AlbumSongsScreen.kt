package com.example.mymusic.ui.screens.album

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mymusic.R
import com.example.mymusic.model.AudioItem
import com.example.mymusic.ui.components.MusicArt
import com.example.mymusic.ui.components.MusicListItem
import com.example.mymusic.ui.viewmodels.HomeViewModel
import com.example.mymusic.utils.ToolbarExitUntilCollapsedState

val MinToolbarHeight = 120.dp
val MaxToolbarHeight = 410.dp

private val musicDummy = AudioItem(
    0L, "Artist", "", "", "", "", "Title", "", 0,
    0, null, null, 0, "", null, 0,
    12334L, ""
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AlbumSongsScreen(
    albumId: String = "",
    homeViewModel: HomeViewModel = hiltViewModel(),
    onNavigation: () -> Unit
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val motionScene = remember {
        context.resources
            .openRawResource(R.raw.album_songs_toolbar_motion_scene)
            .readBytes()
            .decodeToString()
    }
    Log.d("AlbumSongsScreen", "albumId $albumId")

//    var sliderPosition by remember { mutableFloatStateOf(0f) }
    val toolbarHeightRange = with(density) {
        MinToolbarHeight.roundToPx()..MaxToolbarHeight.roundToPx()
    }

    val toolbarState by rememberSaveable(stateSaver = ToolbarExitUntilCollapsedState.Saver) {
        mutableStateOf (ToolbarExitUntilCollapsedState(heightRange = toolbarHeightRange))
    }
    val scrollState = rememberScrollState()
    toolbarState.scrollValue = scrollState.value
    Log.d("AlbumSongsScreen", "scrollState.value ${scrollState.value}")
    Log.d("AlbumSongsScreen", "toolbarState.progress ${toolbarState.progress}")

    val uiState by homeViewModel.uiState
    val album = uiState.getAlbum(albumId)

    // A surface container using the 'background' color from the theme
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onNavigation) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            tint = Color.White,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text(
                        text = "Album",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)) {
            MotionLayout(
                modifier = Modifier
                    .fillMaxSize(),
//                    .background(Color.Green),
                motionScene = MotionScene(motionScene),
                progress = toolbarState.progress,
//                debugFlags = DebugFlags.All
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.LightGray)
                        .layoutId("header_container")
                )
                MusicArt(
                    modifier = Modifier
                        .fillMaxWidth(.6f)
                        .aspectRatio(1f)
                        .layoutId("music_art"),
                    bitmap = album.albumArt
                )
                Column(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .layoutId("album_info_normal"),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = album.name,
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = album.artist,
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .layoutId("album_info_min"),
                ) {
                    Text(
                        text = "Album Name",
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "Album Artist",
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }


                    Button(
                        modifier = Modifier.layoutId("play_button"),
                        shape = RoundedCornerShape(18f),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray
                        ),
                        onClick = { }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.play),
                            tint = Color.White,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Play",
                            style = TextStyle(
                                color = Color.White
                            )
                        )
                    }
                    Button(
                        modifier = Modifier.layoutId("shuffle_button"),
                        shape = RoundedCornerShape(18f),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray
                        ),
                        onClick = { }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.shuffle_variant),
                            tint = Color.White,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Shuffle",
                            style = TextStyle(
                                color = Color.White
                            )
                        )
                    }


                HorizontalDivider(
                    modifier = Modifier.layoutId("divider"),
                    color = Color.Gray
                )
                Column(
                    modifier = Modifier
                        .verticalScroll(
                            state = scrollState,
                            flingBehavior = ScrollableDefaults.flingBehavior()
                        )
//                        .pointerInput(Unit) {
//                            detectVerticalDragGestures(
//                                onVerticalDrag = { change, dragAmount ->
//                                    Log.d(
//                                        "AlbumSongsScreen",
//                                        "change ${change.position} dragAmount ${dragAmount}"
//                                    )
//
//                                },
//                                onDragEnd = {
//                                    Log.d("AlbumSongsScreen", "onDragEnd")
//                                }
//                            )
//                        }
                        .layoutId("content_list_container"),
                ) {
                    album.songs.forEach { song ->
                        MusicListItem(song, click = {})
                    }
                }
            }
//            Slider(
//                modifier = Modifier.align(Alignment.BottomCenter),
//                value = sliderPosition,
//                onValueChange = {
//                    sliderPosition = it
//                }
//            )
        }
    }

}

@Preview(
    showBackground = false,
    showSystemUi = true,
    device = Devices.PIXEL_7
)
@Composable
fun AlbumSongsScreenPreview() {
    AlbumSongsScreen(onNavigation = {  })

}