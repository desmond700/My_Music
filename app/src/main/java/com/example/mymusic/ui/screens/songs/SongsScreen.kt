package com.example.mymusic.ui.screens.songs

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mymusic.ui.components.MusicListItem
import com.example.mymusic.ui.components.SearchAppBar
import com.example.mymusic.ui.viewmodels.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongsScreen(
    onNavigation: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val songList by viewModel.searchedMusicList.collectAsState()

    Log.d("SongsScreen", "songList count ${songList.count()}")

    // A surface container using the 'background' color from the theme
//    Surface(
//        modifier = Modifier.fillMaxSize(),
//        color = MaterialTheme.colorScheme.background,
//    ) {
        Scaffold(
            topBar = {
                SearchAppBar(
                    label = "Songs",
                    onNavigation = onNavigation,
                    placeholderText = "Search for songs",
                    onValueChange = {text ->
                        Log.d("SongsScreen", "onValueChange $text")

                        viewModel.searchQuery.value = text
                    },
                    onClose = {}
                )
            }
        ) {padding ->
            LazyColumn(modifier = Modifier.padding(padding)) {
                itemsIndexed(songList) { index, music ->
                    MusicListItem(
                        artistName = "Unknown Artist",
                        trackName = music.name,
                        duration = music.size
                    )
                }
            }
        }
//    }


}

@Preview(showBackground = true)
@Composable
fun SongsScreenPreview() {
    SongsScreen(onNavigation = {})
}