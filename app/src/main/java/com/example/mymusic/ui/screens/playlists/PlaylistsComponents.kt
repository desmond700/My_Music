package com.example.mymusic.ui.screens.playlists

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mymusic.R
import com.example.mymusic.ui.components.BaseListRowItem
import com.example.mymusic.ui.components.MusicArt

@Composable
fun CreateNewPlaylistButton(click: () -> Unit) {
    BaseListRowItem(
        onTap = { click() },
        horizontalSpacing = 10.dp,
        leading = {
            MusicArt(
                modifier = Modifier.size(60.dp),
                defaultIcon = painterResource(id = R.drawable.playlist_plus)
            )
        },
        title = {
            Text(
                text = "Create new playlist",
                style = MaterialTheme.typography.titleLarge
            )
        }
    )
}

@Composable
fun SelectedPlaylistCount(selected: Int) {
    val selectedAmountText = if (selected > 1) "playlist" else "playlists"
    BaseListRowItem(
        onTap = {  },
        horizontalSpacing = 10.dp,
        leading = {
            MusicArt(
                modifier = Modifier.size(60.dp),
                defaultIcon = painterResource(id = R.drawable.playlist_check)
            )
        },
        title = {
            Text(
                text = "$selected $selectedAmountText selected",
                style = MaterialTheme.typography.titleLarge
            )
        }
    )
}

@Preview
@Composable
fun CreatePlaylistDialog(
    onDismissRequest: () -> Unit = {},
    onConfirmation: (input: String) -> Unit = {}
) {
    var textValue by remember { mutableStateOf(TextFieldValue("")) }

    AlertDialog(
        title = {
            Text(text = "Create a Playlist")
        },
        text = {
            TextField(
                value = textValue,
                placeholder = {
                    Text(text = "Playlist name")
                },
                onValueChange = {
                    Log.d("createPlaylistDialog", "text value ${it.text}")
                    textValue = it
                }
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        dismissButton = {
            TextButton(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.Magenta)
                    .padding(horizontal = 6.dp),
                shape = RoundedCornerShape(8.dp),
                onClick = { onDismissRequest() }
            ) {
                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        confirmButton = {
            TextButton(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
//                    .background(Color.Gray)
                    .padding(horizontal = 6.dp),
                shape = RoundedCornerShape(8.dp),
                onClick = { onConfirmation(textValue.text) }
            ) {
                Text(
                    text = "Confirm",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    )
}