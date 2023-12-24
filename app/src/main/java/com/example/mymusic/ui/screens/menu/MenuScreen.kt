package com.example.mymusic.ui.screens.menu

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymusic.R
import com.example.mymusic.ui.components.ListHeader
import com.example.mymusic.ui.components.ListItemDivider
import com.example.mymusic.ui.screens.menu.ui.theme.MyMusicTheme

@Composable
fun MenuScreen(modifier: Modifier = Modifier) {

    // A surface container using the 'background' color from the theme
    Surface(
        modifier = modifier.padding(end = 80.dp),
        color = Color.Transparent
    ) {
        LazyColumn(
            modifier = Modifier
                .background(Color.DarkGray)
                .fillMaxSize()
        ) {
            item {
                Box(
                    modifier = Modifier
                        .height(140.dp)
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.music),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "My Music",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 30.sp
                            )
                        )
                    }
                }
            }
            item {
                ListItemDivider(modifier = Modifier.padding(bottom = 10.dp))
            }
            item {
                ListHeader()
            }
            items(5) {
                MenuItem(
                    click = {}
                )
            }
            item {
                ListItemDivider(modifier = Modifier.padding(vertical = 15.dp))
            }
            item {
                ListHeader()
            }
            items(2) {
                MenuItem(
                    click = {}
                )
            }
            item {
                ListItemDivider(modifier = Modifier.padding(vertical = 15.dp))
            }
            item {
                ListHeader()
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 20.dp)
                ) {
                    Text(
                        text = "787 MB of 57.96 GB used with audio",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { 0.5f },
                        modifier = Modifier
                            .height(8.dp)
                            .fillMaxWidth(),
                        color = Color.Magenta,
                        trackColor = Color.White,
                        strokeCap = StrokeCap.Round,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "55.27 GB used • 2.69 GB available",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}





@Composable
fun MenuItem(click: () -> Unit) {
    Row(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .clickable { click() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(18.dp))
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = Icons.Outlined.FavoriteBorder,
            tint = Color.White,
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = "Tracks",
            style = TextStyle(
                color = Color.White,
                fontSize = 20.sp,
            )
        )
        Spacer(modifier = Modifier.width(18.dp))
    }
}

@Preview(showBackground = true)
@Preview(
    name = "Dark mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun MenuScreenPreview() {
    MyMusicTheme {
        MenuScreen()
    }
}