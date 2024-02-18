package com.example.mymusic.ui.screens.artists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.mymusic.R
import com.example.mymusic.data.room.relationship.ArtistWithSongs
import com.example.mymusic.ui.components.MusicArt
import com.example.mymusic.ui.components.MusicListItem
import com.example.mymusic.ui.viewmodels.ArtistDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ArtistSongsScreen(
    navController: NavHostController,
    artistData: ArtistWithSongs,
    viewModel: ArtistDetailsViewModel = hiltViewModel(),
    onNavigation: () -> Unit
) {

//    val context = LocalContext.current
//    val motionScene =

    val artistWithSongs by viewModel.artistWithSongs.collectAsStateWithLifecycle()

    val artist = artistWithSongs.artist
    val songs = artistWithSongs.songs

    // A surface container using the 'background' color from the theme
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
//        MotionLayout(
//            motionScene = ,
//            progress =
//        ) {
//
//        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .height(450.dp)
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MusicArt(
                    modifier = Modifier
                        .fillMaxWidth(.6f)
                        .aspectRatio(1f)
                        .align(Alignment.CenterHorizontally),
                    bitmap = null
                )
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Album Name",
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "Album Artist",
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp),
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    maxItemsInEachRow = 2
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
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
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(18f),
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
                }
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = Color.Gray)
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(songs){ audio ->
                    MusicListItem(
                        audio,
                        click = {},
                        onAddToPlaylist = {},
                        onAddToFavourites = {}
                    )
                }
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun ArtistSongsScreenPreview() {
//    ArtistSongsScreen(onNavigation = {})
//}