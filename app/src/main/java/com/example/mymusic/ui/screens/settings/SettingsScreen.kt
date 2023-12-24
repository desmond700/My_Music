package com.example.mymusic.ui.screens.settings

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.example.mymusic.ui.components.ListHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {

    // A surface container using the 'background' color from the theme
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            )
        }
    ) {innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize(),
            contentPadding = PaddingValues(0.dp)
        ) {
            item {
                ListHeader(
                    modifier = Modifier.padding(start = 1.dp),
                )
            }
            item {
                ListItemContainer {
                    Array(5) {
                        ListItem(click = {})
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(15.dp))
            }
            item {
                ListHeader(
                    modifier = Modifier.padding(start = 1.dp),
                )
            }
            item {
                ListItemContainer {
                    Array(5) {
                        ListItem(click = {})
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }


}

@Composable
fun ListItemContainer(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(18.dp),
                clip = true
            )
            .background(Color("#45475E".toColorInt()))
    ) {
        Column(
            modifier = modifier
                .padding(vertical = 15.dp)
        ) {
            content()
        }
    }
}

@Composable
fun ListItem(click: () -> Unit) {
    Row(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .clickable { click() },
        verticalAlignment = Alignment.CenterVertically
    ) {
//        Icon(
//            modifier = Modifier.size(24.dp),
//            imageVector = Icons.Outlined.FavoriteBorder,
//            tint = Color.Black,
//            contentDescription = null
//        )
        Spacer(modifier = Modifier.width(28.dp))
        Text(
            text = "Tracks",
            style = TextStyle(
                color = Color.Black,
                fontSize = 20.sp,
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = true,
            onCheckedChange = {}
        )
        Spacer(modifier = Modifier.width(12.dp))
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}