package com.example.mymusic.ui.screens.album

import android.annotation.SuppressLint
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
import com.example.mymusic.ui.components.AlbumListItem
import com.example.mymusic.ui.components.LoadingContent
import com.example.mymusic.ui.viewmodels.AlbumListViewModel

@SuppressLint("Range")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumsScreen(
    navController: NavHostController,
    viewModel: AlbumListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val albumList = uiState.albums

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
                        text = "Albums",
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
            empty = albumList.isEmpty(),
            loading = uiState.isLoading,
            onRefresh = {viewModel.refreshAlbums()}
        ) {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(items = albumList) { album ->
                    AlbumListItem(
                        albumData = album,
                        click = { navController.navigate("albums/${album.album.albumId}") }
                    )
                }
            }
        }

    }


}

//@Preview(showBackground = true)
//@Composable
//fun AlbumsScreenPreview() {
//    AlbumsScreen(
//        onNavigation = { },
//        onNavigationAlbumSongs = { },
//        viewModel = hiltViewModel()
//    )
//}