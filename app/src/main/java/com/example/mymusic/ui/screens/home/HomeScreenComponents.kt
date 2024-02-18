package com.example.mymusic.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mymusic.data.room.entities.Album
import com.example.mymusic.data.room.entities.Artist
import com.example.mymusic.data.room.entities.Song
import com.example.mymusic.data.room.relationship.PlaylistWithSongs
import com.example.mymusic.ui.components.MusicArt
import com.example.mymusic.utils.getArtworks
import com.example.mymusic.utils.toImageBitmap

@Composable
fun BaseActionItem(
    bitmap: ImageBitmap? = null,
    bitmaps: List<ImageBitmap>? = null,
    titleText: String,
    subTitleText: String,
    click: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(150.dp)
            .padding(10.dp)
            .clickable { click() }
    ) {
        Surface(
            modifier = Modifier.aspectRatio(1f),
            color = Color.DarkGray,
            shape = RoundedCornerShape(10.dp),
            shadowElevation = 8.dp
        ) {
            MusicArt(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(0f),
                shadowElevation = 0.dp,
                color = Color.Red,
                bitmap = bitmap,
                bitmaps = bitmaps
            )
        }
        Column(
            modifier = Modifier
                .padding(top = 5.dp, start = 5.dp, end = 5.dp)
        ) {
            Text(
                text = titleText,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subTitleText,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.labelMedium.copy(color = Color.Gray)
            )
        }

    }
}

@Composable
fun SongActionItem(
    song: Song,
    click: () -> Unit
) {
    BaseActionItem(
        titleText = song.title,
        subTitleText = song.artistName,
        bitmap = song.artworkBlob?.toImageBitmap(),
        click = click
    )
}

@Composable
fun ArtistActionItem(
    artist: Artist,
    click: () -> Unit
) {
    BaseActionItem(
        titleText = artist.artistName,
        subTitleText = artist.artistName,
        bitmap = artist.artwork?.toImageBitmap(),
        click = click
    )
}

@Composable
fun AlbumActionItem(
    album: Album,
    click: () -> Unit
) {
    BaseActionItem(
        titleText = album.albumName,
        subTitleText = album.albumArtist,
        bitmap = album.albumArt?.toImageBitmap(),
        click = click
    )
}

@Composable
fun PlaylistActionItem(
    playlistWithSongs: PlaylistWithSongs,
    click: () -> Unit
) {
    val (playlist) = playlistWithSongs

    BaseActionItem(
        titleText = playlist.playlistName,
        subTitleText = playlist.playlistName,
        bitmaps = playlistWithSongs.getArtworks(),
        click = click
    )
}