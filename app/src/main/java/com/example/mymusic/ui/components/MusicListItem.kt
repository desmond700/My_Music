package com.example.mymusic.ui.components

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymusic.R
import com.example.mymusic.data.room.entities.Song
import com.example.mymusic.data.room.relationship.AlbumWithSongs
import com.example.mymusic.data.room.relationship.ArtistWithSongs
import com.example.mymusic.utils.MenuButtonIconItem
import com.example.mymusic.utils.MenuButtonItemDivider
import com.example.mymusic.utils.MenuButtonListItem
import com.example.mymusic.utils.formatDuration
import com.example.mymusic.utils.toImageBitmap

@Composable
fun MusicListItem(
    song: Song,
    click: () -> Unit,
    onAddToPlaylist: () -> Unit,
    onAddToFavourites: () -> Unit
) {

    BaseListRowItem(
        onTap = {
            Log.d("SongsScreen", "SongsScreenContent onClick ")
            click()
        },
        horizontalSpacing = 10.dp,
        verticalSpacing = 10.dp,
        leading = {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .padding(5.dp)
                    .clip(shape = RoundedCornerShape(5.dp))
                    .background(Color.Blue),
                contentAlignment = Alignment.Center
            ) {
                if (song.artworkBlob != null) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        bitmap = song.artworkBlob.toImageBitmap(),
                        contentScale = ContentScale.FillBounds,
                        contentDescription = null
                    )
                }
                else {
                    Icon(
                        modifier = Modifier
                            .height(25.dp)
                            .width(25.dp),
                        painter = painterResource(id = R.drawable.music_note),
                        contentDescription = null
                    )
                }
            }
        },
        title = {
            Text(
//                        modifier = Modifier.background(Color.DarkGray),
                text = song.getArtist(),
                style = TextStyle(
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        subTitle = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = song.title,
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    text = song.duration.formatDuration(),
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        trailing = {
            MenuButton(
                click = {  },
                items = listOf (
                    MenuButtonListItem(
                        title = "Add to a Playlist",
                        icon = MenuButtonIconItem(
                            imageVector = Icons.AutoMirrored.Filled.PlaylistPlay,
                            contentDescription = "Add to playlist icon"
                        ),
                        click = onAddToPlaylist
                    ),
                    MenuButtonItemDivider(
                        divider = { HorizontalDivider() }
                    ),
                    MenuButtonListItem(
                        title = "Add to Favourites",
                        icon = MenuButtonIconItem(
                            imageVector = Icons.Filled.Favorite,
                            color = if (song.favourite) Color.Red else Color.DarkGray,
                            contentDescription = "Add to favourites icon"
                        ),
                        click = onAddToFavourites
                    ),
                    MenuButtonListItem(
                        title = "Edit Artwork",
                        icon = MenuButtonIconItem(
                            imageVector = Icons.Filled.Favorite,
                            color = if (song.favourite) Color.Red else Color.DarkGray,
                            contentDescription = "Edit Artwork icon"
                        ),
                        click = onAddToFavourites
                    )
                )
            )
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BaseListRowItem(
    onTap: () -> Unit,
    onLongPress: (() -> Unit)? = null,
    horizontalSpacing: Dp = 0.dp,
    verticalSpacing: Dp = 0.dp,
    leading: @Composable (() -> Unit)? = null,
    title: @Composable () -> Unit,
    subTitle: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
) {
    Surface(
        modifier = Modifier
            .height(75.dp)
            .combinedClickable (
                onClick = onTap,
                onLongClick = onLongPress
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = horizontalSpacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leading != null) {
                leading()
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp, end = 6.dp),
                verticalArrangement = Arrangement.Center
            ) {
                title()
                Spacer(modifier = Modifier.height(verticalSpacing))
                if (subTitle != null) {
                    subTitle()
                }
            }
            if (trailing != null) {
                trailing()
            }
        }
    }
}

@Composable
fun BaseListTileItem() {

}

@Composable
fun AlbumListItem(
    modifier: Modifier = Modifier,
    albumData: AlbumWithSongs,
    click: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .clickable { click() }
    ) {
        Surface(
            modifier = modifier.aspectRatio(1f),
            color = Color.DarkGray,
            shape = RoundedCornerShape(10.dp),
            shadowElevation = 8.dp
        ) {
            MusicArt(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(0f),
                shadowElevation = 0.dp,
                color = Color.Red,
                bitmap = albumData.album.albumArt?.toImageBitmap()
            )
        }
        Column(
            modifier = Modifier
                .padding(top = 5.dp, start = 5.dp, end = 5.dp)
        ) {
            Text(
                modifier = Modifier.padding(bottom = 2.dp),
                text = albumData.album.albumName,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = albumData.album.albumArtist,
                style = MaterialTheme.typography.titleMedium
            )
        }

    }
}

@Composable
fun ArtistListItem(
    modifier: Modifier = Modifier,
    artistData: ArtistWithSongs,
    click: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .clickable { click() }
    ) {
        Surface(
            modifier = modifier.aspectRatio(1f),
            color = Color.DarkGray,
            shape = RoundedCornerShape(10.dp),
            shadowElevation = 8.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(72.dp),
                    painter = painterResource(id = R.drawable.music),
                    contentDescription = null
                )
            }
        }
        Column(
            modifier = Modifier
                .padding(top = 5.dp, start = 5.dp, end = 5.dp)
        ) {
            Text(
                modifier = Modifier.padding(bottom = 2.dp),
                text = artistData.artist.artistName,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "${artistData.songs.size} tracks",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = Color.Gray
                )
            )
        }

    }
}