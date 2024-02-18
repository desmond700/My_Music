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
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.layoutId
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import com.example.mymusic.R
import com.example.mymusic.ui.components.MusicArt
import com.example.mymusic.ui.viewmodels.MusicPlayerViewModel
import com.example.mymusic.ui.viewmodels.MusicPlayerViewModelState
import com.example.mymusic.utils.formatDuration
import com.example.mymusic.utils.toImageBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicArtwork(
    uiState: MusicPlayerViewModelState,
    dragState: AnchoredDraggableState<DragAnchors>,
    isPlayingState: StateFlow<Boolean>
) {
    val isPlaying by isPlayingState.collectAsState()
    val animatedScale by animateFloatAsState(
        targetValue = when(dragState.currentValue) {
            DragAnchors.Start -> 1f
            else -> if (isPlaying) 1.2f else 1f
        },
        animationSpec = tween(
            durationMillis = 500,
            easing = EaseInBounce
        ),
        label = "scale"
    )
    Log.d("MusicArtwork", "isPlaying $isPlaying")

    MusicArt(
        modifier = Modifier
            .layoutId("music_art")
            .graphicsLayer {
                this.scaleX = animatedScale
                this.scaleY = animatedScale
            }
            .anchoredDraggable(
                state = dragState,
                reverseDirection = true,
                orientation = Orientation.Vertical
            ),
        bitmap = uiState.currentSong!!.artworkBlob?.toImageBitmap()
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

    Log.d("MusicTitleColumnNormal", "artist $artist, title $title")

    Column(
        modifier = Modifier.layoutId("music_title_normal"),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier
                .basicMarquee()
                .padding(start = 20.dp),
            text = artist,
            style = TextStyle(
                color = Color.White,
                fontSize = 21.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            modifier = Modifier
                .basicMarquee()
                .padding(start = 20.dp),
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
                    Text(
                        text = stringResource(id = R.string.add_to_playlist)
                    )
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
                    Text(
                        text = stringResource(id = R.string.add_to_favourites)
                    )
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
    viewModel: MusicPlayerViewModel,
    onValueChange: (value: Float) -> Unit
) {
    val duration by viewModel.duration.collectAsStateWithLifecycle()
    val progressValue by viewModel.progress.collectAsStateWithLifecycle()

//    Log.d("PlayerScreen", "SeekBar progress $progress")
//    Log.d("PlayerScreen", "SeekBar duration $duration")
//    Log.d("PlayerScreen", "SeekBar value $value")

    Slider(
        modifier = Modifier.layoutId("seekbar"),
        value = progressValue.toFloat(),
        onValueChange = { value ->
            Log.d("SeekBar", "onValueChange value $value")
            onValueChange(value)
        },
        track = { sliderState ->
            SliderDefaults.Track(
                modifier = Modifier.scale(scaleX = 1f, scaleY = 1.5f),
                sliderState = sliderState
            )
        },
        valueRange = 0f..duration.toFloat()
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
fun TrackPlayBackPositionText(
    uiState: MusicPlayerViewModelState,
    progress: StateFlow<Long>
) {
    val progressValue by progress.collectAsState()
    val progressString = progressValue.formatDuration()
    Log.d("TrackPlayBackPositionText", "progress $progress")
    Text(
        modifier = Modifier.layoutId("track_playback_position_text"),
        text = progressString,
        style = TextStyle(
            color = Color.DarkGray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    )
}

@Composable
fun TrackDurationText(viewModel: MusicPlayerViewModel) {
    val duration by viewModel.duration.collectAsStateWithLifecycle()

    Text(
        modifier = Modifier.layoutId("track_duration_text"),
        text = duration.formatDuration(),
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
    click: () -> Unit
) {
    val shuffleIcon: Int = when(uiState.shuffleModeEnabled) {
        true -> R.drawable.shuffle
        else -> R.drawable.shuffle_disabled
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
fun PlayButton(
    isPlayingState: StateFlow<Boolean>,
    click: () -> Unit
) {
    val isPlaying by isPlayingState.collectAsState()
    val playStateIcon: Int = when (isPlaying) {
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
    uiState: MusicPlayerViewModelState,
    click: () -> Unit
) {
    val repeatModeIcon = when(uiState.repeatMode) {
        Player.REPEAT_MODE_ONE -> R.drawable.repeat_once
        Player.REPEAT_MODE_ALL -> R.drawable.repeat
        else -> R.drawable.repeat_off
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