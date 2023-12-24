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
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
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
import com.example.mymusic.R
import com.example.mymusic.ui.viewmodels.MusicPlayerViewModel
import com.example.mymusic.ui.viewmodels.MusicPlayerViewModelState
import com.example.mymusic.ui.viewmodels.UIEvents
import kotlinx.coroutines.launch

enum class DragAnchors {
    Start,
    End
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
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

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun MusicPlayerScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MusicPlayerViewModel,
    snackbarHostState: SnackbarHostState,
    content: @Composable (modifier: Modifier) -> Unit
) {
    val uiState by viewModel.uiState
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

    val swipeProgress = state.requireOffset() / screenHeight;

    val motionScene = remember {
        context.resources
            .openRawResource(R.raw.player_motion_scene)
            .readBytes()
            .decodeToString()

    }

    val statusBarTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    uiState.currentSong?.let {
       scope.launch {
           Log.d("MusicPlayerScreen", "animateTo DragAnchors.End state ${state.requireOffset()}")
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
            content(Modifier.layoutId("Home_screen"))

            uiState.currentSong?.let {
                Box( // Background container
                    modifier = Modifier
                        .background(Color.Magenta)
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
                    state = state
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
                    uiState = uiState,
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
                TrackPlayBackPositionText(uiState = uiState)
            }

            uiState.currentSong?.let {
                TrackDurationText(uiState = uiState)
            }

            uiState.currentSong?.let {
                ShuffleButton(
                    uiState = uiState,
                    scope = scope,
                    snackBarHostState = snackbarHostState,
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
                    uiState = uiState,
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
                    snackBarHostState = snackbarHostState,
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