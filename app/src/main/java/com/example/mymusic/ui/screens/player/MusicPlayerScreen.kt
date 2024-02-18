package com.example.mymusic.ui.screens.player

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.constraintlayout.compose.layoutId
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.palette.graphics.Palette
import com.example.mymusic.R
import com.example.mymusic.ui.viewmodels.MusicPlayerViewModel
import com.example.mymusic.ui.viewmodels.MusicPlayerViewModelState
import com.example.mymusic.ui.viewmodels.UIEvents
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

enum class DragAnchors {
    Start,
    End
}

private fun getBackgroundGradient(palette: Palette?): Brush {
    val startColor = palette?.dominantSwatch?.rgb?.let { Color(it) } ?: Color.Magenta
    val endColor = palette?.darkMutedSwatch?.rgb?.let { Color(it) } ?: Color.Magenta

    return Brush.linearGradient(colors = listOf(startColor, endColor))
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier,
    progress: Float,
    viewModel: MusicPlayerViewModel = hiltViewModel(),
    content: @Composable (modifier: Modifier) -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        MusicPlayerScreenContent(
            modifier = modifier,
            viewModel = viewModel,
            snackbarHostState = snackbarHostState,
            content = content
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun MusicPlayerScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MusicPlayerViewModel,
    snackbarHostState: SnackbarHostState,
    content: @Composable (modifier: Modifier) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    // Calculate screen height
    val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx() }

    val state = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.Start,
            positionalThreshold = { totalDistance: Float -> totalDistance * 0.5f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            animationSpec = tween()
        ).apply {
            updateAnchors(
                DraggableAnchors {
                    DragAnchors.Start at 0f
                    DragAnchors.End at screenHeight
                }
            )
        }
    }

    LaunchedEffect(snackbarMessage) {
        scope.launch {
            if (snackbarMessage != null) {
                snackbarHostState.showSnackbar(
                    message = snackbarMessage!!
                )
            }
        }
    }



    Log.d("MusicPlayerScreenContent", "state.currentValue ${state.currentValue}")


    val swipeProgress = state.requireOffset() / screenHeight

    val motionScene = remember {
        context.resources
            .openRawResource(R.raw.player_motion_scene)
            .readBytes()
            .decodeToString()

    }

    val statusBarTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

//    uiState.currentSong?.let {
//       scope.launch {
//           Log.d("MusicPlayerScreen", "animateTo DragAnchors.End state ${state.requireOffset()}")
//           state.animateTo(DragAnchors.End)
//       }
//    }

    LaunchedEffect(Unit) {
        scope.launch {
            viewModel.snackbarMessage.collectLatest { message ->
                Log.d("PlayerScreen", "snackbarMessage collectLatest")

                if (message != null) {
                    snackbarHostState.showSnackbar(
                        message = message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }

    }

    LaunchedEffect(uiState.currentSong) {
        Log.d("MusicPlayerScreenContent", "state.currentValue ${state.currentValue}")
        Log.d("MusicPlayerScreenContent", "currentSong ${uiState.currentSong}")
        if (uiState.currentSong != null && state.currentValue == DragAnchors.Start) {
            state.animateTo(DragAnchors.End)
        }
    }

    Column(modifier = modifier
        .fillMaxSize()
        .background(Color.Blue)
    ) {
//        val (motionLayout, statusBarBg) = createRefs()
//        Box(
//            modifier = Modifier
//                .height(statusBarTop)
//                .fillMaxWidth()
//                .background(Color.Magenta)
////                .constrainAs(statusBarBg) {
////                    top.linkTo(parent.top)
////                    start.linkTo(parent.start)
////                    end.linkTo(parent.end)
////                }
//        )
        MotionLayout(
            modifier = Modifier
                .fillMaxSize()
                .pointerInteropFilter {
                    // Return false here to pass down the MotionEvent
                    return@pointerInteropFilter false
                },
            motionScene = MotionScene(content = motionScene),
            progress = swipeProgress,
        ) {
            val paddingProperty = customProperties(id = "Home_screen")

            content(Modifier
                .padding(bottom = when(uiState.currentSong) {
                        null -> 0.dp
                        else -> paddingProperty.float("padding").dp
                    }
                )
                .layoutId("Home_screen")
            )

            uiState.currentSong?.let {
                Box( // Background container
                    modifier = Modifier
                        .background(getBackgroundGradient(uiState.colorPalette))
                        .layoutId("bg_container")
                        .anchoredDraggable(
                            state = state,
                            reverseDirection = true,
                            orientation = Orientation.Vertical
                        )
                )
            }

            uiState.currentSong?.let {
                DragIndicator()
            }

            uiState.currentSong?.let {
                MusicArtwork(
                    uiState = uiState,
                    isPlayingState = viewModel.isPlaying,
                    dragState = state
                )
            }

            uiState.currentSong?.let {
                MusicTitleColumnNormal(uiState = uiState)
            }

            uiState.currentSong?.let {
                MusicTitleColumnMin(uiState = uiState)
            }

            uiState.currentSong?.let {
                PlayerMenuButton(click = {})
            }

            uiState.currentSong?.let {
                SeekBar(
                    viewModel = viewModel,
                    onValueChange = { pos -> viewModel.onPlayerUIEvents(UIEvents.SeekTo(pos)) }
                )
            }

            uiState.currentSong?.let {
                VolumeLowIcon()
            }

            uiState.currentSong?.let {
                VolumeSeekBar(
                    uiState = uiState,
                    onValueChange = {}
                )
            }

            uiState.currentSong?.let {
                VolumeHighIcon()
            }

            uiState.currentSong?.let {
                TrackPlayBackPositionText(
                    uiState = uiState,
                    progress = viewModel.progress
                )
            }

            uiState.currentSong?.let {
                TrackDurationText(viewModel = viewModel)
            }

            uiState.currentSong?.let {
                ShuffleButton(
                    uiState = uiState,
                    scope = scope,
                    click = {
                        viewModel.onPlayerUIEvents(UIEvents.Shuffle)
                    }
                )
            }

            uiState.currentSong?.let {
                SkipPreviousButton(click = {
                    viewModel.onPlayerUIEvents(UIEvents.Previous)
                })
            }

            uiState.currentSong?.let {
                PlayButton(
                    isPlayingState = viewModel.isPlaying,
                    click = {
                        viewModel.onPlayerUIEvents(UIEvents.PlayPause)
                    }
                )
            }

            uiState.currentSong?.let {
                SkipNextButton(click = {
                    viewModel.onPlayerUIEvents(UIEvents.Next)
                })
            }

            uiState.currentSong?.let {
                ReplayButton(
                    uiState = uiState,
                    scope = scope,
                    click = {
                        viewModel.onPlayerUIEvents(UIEvents.Repeat)
                    }
                )
            }

        }
    }
}

@Preview(
    showBackground = true,
    device = Devices.PIXEL_7,
    backgroundColor = 0xFF7D5260
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PlayerScreenPreview2() {
    PlayerScreen(progress = 0f, content = {})
}

@Preview
@Composable
fun VolumeSeekBarPreview() {
    VolumeSeekBar(
        uiState =  MusicPlayerViewModelState(),
        onValueChange = {}
    )
}