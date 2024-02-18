package com.example.mymusic.ui.screens.artists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.mymusic.ui.components.ArtistListItem
import com.example.mymusic.ui.components.LoadingContent
import com.example.mymusic.ui.viewmodels.ArtistListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistsScreen(
    navController: NavHostController,
    viewModel: ArtistListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val artistList = uiState.artists

    // A surface container using the 'background' color from the theme
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier
//                        .height(60.dp)
                    .zIndex(1f),
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            tint = Color.White,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text(
                        text = "Artists",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            tint = Color.White,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LoadingContent(
            empty = artistList.isEmpty(),
            loading = uiState.isLoading,
            onRefresh = {viewModel.refreshData()}
        ) {
            LazyVerticalGrid(
                modifier = Modifier
                    .padding(top = innerPadding.calculateTopPadding())
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(items = artistList) {artist ->
                    ArtistListItem(
                        artistData = artist,
                        click = { navController.navigate("artists/${artist.artist.artistId}") }
                    )
                }
            }
        }

    }

}

//@Preview(showBackground = true)
//@Composable
//fun ArtistScreenPreview4() {
//    MyMusicTheme {
//        ArtistsScreen(
//            viewModel = hiltViewModel()
//        )
//    }
//}