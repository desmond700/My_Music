package com.example.mymusic.ui.screens.player

import android.util.Log
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.layoutId
import androidx.media3.common.Player
import com.example.mymusic.R
import com.example.mymusic.ui.components.MusicArt
import com.example.mymusic.ui.viewmodels.MusicPlayerViewModelState
import com.example.mymusic.utils.formatDuration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicArtwork(
    uiState: MusicPlayerViewModelState,
    state: AnchoredDraggableState<DragAnchors>,
) {
    val isPlaying = uiState.isPlaying
    val animatedScale by animateFloatAsState(
        targetValue = if (isPlaying) 1.2f else 1f,
        animationSpec = tween(
            durationMillis = 500,
            easing = EaseInBounce
        ),
        label = "scale"
    )


    MusicArt(
        modifier = Modifier
            .layoutId("music_art")
            .graphicsLayer {
                this.scaleX = animatedScale
                this.scaleY = animatedScale
            }
            .anchoredDraggable(
                state = state,
                reverseDirection = true,
                orientation = Orientation.Vertical
            ),
        bitmap = uiState.currentSong!!.getArtworkBitmap()
    )
}

@Composable
fun DragIndicator() {
    Box(
        modifier = Modifier
            .layoutId("drag_indicator")
            .clip(shape = RoundedCornerShape(4.dp))
            .background(Color.DarkGray)
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicTitleColumnNormal(uiState: MusicPlayerViewModelState) {
    val artist = uiState.currentSong!!.getArtist()
    val title = uiState.currentSong.title

    Column(
        modifier = Modifier.layoutId("music_title_normal"),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier.basicMarquee(),
            text = artist,
            style = TextStyle(
                color = Color.White,
                fontSize = 21.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            modifier = Modifier.basicMarquee(),
            text = title,
            style = TextStyle(
                color = Color.LightGray,
                fontSize = 19.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicTitleColumnMin(uiState: MusicPlayerViewModelState) {
    val artist = uiState.currentSong!!.getArtist()
    val title = uiState.currentSong.title

    Column(
        modifier = Modifier.layoutId("music_title_min"),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier.basicMarquee(),
            text = artist,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            modifier = Modifier.basicMarquee(),
            text = title,
            maxLines = 1,
            style = TextStyle(
                color = Color.LightGray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

@Composable
fun PlayerMenuButton(click: () -> Unit) {
    var menuExpanded by remember {
        mutableStateOf(false)
    }

    Box(modifier = Modifier.layoutId("player_menu_button")) {
        IconButton(
            modifier = Modifier
                .size(30.dp)
                .clip(shape = CircleShape)
                .padding(0.dp)
                .background(Color.DarkGray),
            onClick = {
                click()
                menuExpanded = true
            }
        ) {
            Icon(
                //            modifier = Modifier.size(15.dp),
                imageVector = Icons.Rounded.MoreHoriz,
                contentDescription = null
            )
        }
        DropdownMenu(
            modifier = Modifier.widthIn(min = 230.dp),
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false },
        ) {
            // 6
            DropdownMenuItem(
                text = {
                    Text("Add to a Playlist")
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.PlaylistPlay,
                        contentDescription = null
                    )
                },
                onClick = { /* TODO */ },
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = {
                    Text("Add to Favourites")
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null
                    )
                },
                onClick = { /* TODO */ },
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = {
                    Text("About")
                },
                onClick = { /* TODO */ },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeekBar(
    uiState: MusicPlayerViewModelState,
    onValueChange: (value: Float) -> Unit
) {
    val duration = uiState.duration.toFloat()
    val progress = uiState.progress.toFloat()

//    Log.d("PlayerScreen", "SeekBar progress $progress")
//    Log.d("PlayerScreen", "SeekBar duration $duration")
//    Log.d("PlayerScreen", "SeekBar value $value")

    Slider(
        modifier = Modifier.layoutId("seekbar"),
        value = progress,
        onValueChange = { value ->
            onValueChange(value)
        },
        track = { sliderState ->
            SliderDefaults.Track(
                modifier = Modifier.scale(scaleX = 1f, scaleY = 1.5f),
                sliderState = sliderState
            )
        },
        valueRange = 0f..duration
    )
}

@Composable
fun VolumeLowIcon() {
    Box(modifier = Modifier.layoutId("volume_low_icon")) {
        Icon(
            modifier = Modifier
                .size(24.dp)
                ,
            painter = painterResource(id = R.drawable.volume_low),
            tint = Color.DarkGray,
            contentDescription = null
        )
    }
}

@Composable
fun VolumeHighIcon() {
    Icon(
        modifier = Modifier
            .size(24.dp)
            .layoutId("volume_high_icon"),
        painter = painterResource(id = R.drawable.volume_high),
        tint = Color.Black,
        contentDescription = null
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VolumeSeekBar(
    uiState: MusicPlayerViewModelState,
    onValueChange: (value: Float) -> Unit
) {
    Slider(
        modifier = Modifier
            .height(25.dp)
            .layoutId("volume_seekbar"),
        value = 0f,
        thumb = {},
        track = { sliderState ->
            SliderDefaults.Track(
                modifier = Modifier.scale(scaleX = 1f, scaleY = 1.5f),
                sliderState = sliderState
            )
        },
        onValueChange = {}
    )
}

@Composable
fun TrackPlayBackPositionText(uiState: MusicPlayerViewModelState) {
    val progress = uiState.progress
    Log.d("PlayerScreen", "TrackPlayBackPositionText progress ${progress.formatDuration()}")
    Text(
        modifier = Modifier.layoutId("track_playback_position_text"),
        text = progress.formatDuration(),
        style = TextStyle(
            color = Color.DarkGray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    )
}

@Composable
fun TrackDurationText(uiState: MusicPlayerViewModelState) {
    val duration = uiState.duration.formatDuration()

    Text(
        modifier = Modifier.layoutId("track_duration_text"),
        text = duration,
        style = TextStyle(
            color = Color.DarkGray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    )
}

@Composable
fun ShuffleButton(
    uiState: MusicPlayerViewModelState,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    click: () -> Unit
) {
    val shuffleIcon: Int = when(uiState.shuffleModeEnabled) {
        true -> {
            scope.launch {
//                snackBarHostState.showSnackbar(
//                    message = "Shuffle on"
//                )
            }
            R.drawable.shuffle
        }
        else -> {
            scope.launch {
//                snackBarHostState.showSnackbar(
//                    message = "Shuffle off"
//                )
            }
            R.drawable.shuffle_disabled
        }
    }

    Surface(
        modifier = Modifier
            .clickable { click() }
            .layoutId("music_shuffle_button"),
        shape = CircleShape,
        color = Color.Transparent,
//        shadowElevation = 10.dp
    ) {
        Icon(
            modifier = Modifier.padding(8.dp),
            painter = painterResource(id = shuffleIcon),
            tint = Color.Black,
            contentDescription = "Music Player shuffle button"
        )
    }
}

@Composable
fun SkipPreviousButton(click: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        modifier = Modifier
            .layoutId("music_previous_button")
            .clickable { click() },
//        shape = CircleShape,
        color = Color.Transparent,
//        shadowElevation = 10.dp
    ) {
        Icon(
            modifier = Modifier.padding(4.dp),
            painter = painterResource(id = R.drawable.skip_previous),
            tint = Color.Black,
            contentDescription = null
        )
    }
}

@Composable
fun PlayButton(uiState: MusicPlayerViewModelState, click: () -> Unit) {
    val playStateIcon: Int = when (uiState.isPlaying) {
        true -> R.drawable.pause
        else -> R.drawable.play
    }

    Surface(
        modifier = Modifier
//            .clip(CircleShape)
//            .background(Color.Black)
            .clickable { click() }
            .layoutId("music_play_button"),
        color = Color.Transparent,
    ) {
        Icon(
            modifier = Modifier.size(50.dp),
            painter = painterResource(id = playStateIcon),
            tint = Color.Black,
            contentDescription = null
        )
    }
}

@Composable
fun SkipNextButton(click: () -> Unit) {
//    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        modifier = Modifier
            .clickable { click() }
            .height(64.dp)
//            .padding(3.dp)
            .layoutId("music_next_button"),
//        shape = CircleShape,
        color = Color.Transparent,
//        shadowElevation = 10.dp
    ) {
        Icon(
            modifier = Modifier.padding(4.dp),
            painter = painterResource(id = R.drawable.skip_next),
            tint = Color.Black,
            contentDescription = "Music Player next media item button"
        )
    }
}

@Composable
fun ReplayButton(
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    uiState: MusicPlayerViewModelState,
    click: () -> Unit
) {
    val repeatModeIcon = when(uiState.repeatMode) {
        Player.REPEAT_MODE_ONE -> {
            scope.launch {
                snackBarHostState.showSnackbar(
                    message = "Repeat 1",
                    actionLabel = null
                )
            }
            R.drawable.repeat_once
        }
        Player.REPEAT_MODE_ALL -> {
            scope.launch {
//                snackBarHostState.showSnackbar(
//                    message = "Repeat All",
//                    actionLabel = null
//                )
            }
            R.drawable.repeat
        }
        else -> {
            scope.launch {
//                snackBarHostState.showSnackbar(
//                    message = "Repeat Off",
//                    actionLabel = null
//                )
            }
            R.drawable.repeat_off
        }
    }

    Surface(
        modifier = Modifier
            .size(50.dp)
            .clickable { click() }
            .layoutId("music_replay_button"),
//        shape = CircleShape,
        color = Color.Transparent,
//        shadowElevation = 10.dp
    ) {
        Icon(
            modifier = Modifier.padding(8.dp),
            painter = painterResource(id = repeatModeIcon),
            tint = Color.Black,
            contentDescription = "Music Player repeat button"
        )
    }
}