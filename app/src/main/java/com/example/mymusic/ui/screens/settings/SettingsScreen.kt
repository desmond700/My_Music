package com.example.mymusic.ui.screens.settings

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mymusic.ui.components.BaseListRowItem
import com.example.mymusic.ui.components.ListHeader
import com.example.mymusic.ui.viewmodels.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {

    val settingsState by settingsViewModel.settingsState.collectAsStateWithLifecycle()

    // A surface container using the 'background' color from the theme
    Scaffold(
        modifier = modifier.fillMaxSize(),
//        containerColor = MaterialTheme.colorScheme.background,
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
//            stickyHeader {  }
            item {
                ListHeader(
                    modifier = Modifier.padding(start = 1.dp),
                )
            }
            item {
                ListItemContainer {
                    ListItem(
                        title = "Dark mode",
                        value = settingsState.darkMode,
                        onValueChanged = { value ->
                            settingsViewModel.setDarkMode(value)
                        }
                    )

                    ListItem(
                        title = "Play immediately",
                        value = settingsState.playImmediately,
                        onValueChanged = { value ->
                            settingsViewModel.playSongImmediately(value)
                        }
                    )

                }
            }
        }
    }


}

@Composable
fun ListItemContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .padding(horizontal = 12.dp)
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(8.dp),
                clip = true
            )
            .background(Color.DarkGray)
    ) {
        Column {
            content()
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun ListItem(
    title: String,
    value: Boolean,
    onValueChanged: (Boolean) -> Unit
) {
    var checked by remember { mutableStateOf(value) }

    BaseListRowItem(
        horizontalSpacing = 5.dp,
        onTap = {
            checked = checked.not()
            onValueChanged(checked)
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        },
        trailing = {
            SettingsSwitch(
                checked = value,
                onCheckedChange = {
                    checked = it
                    onValueChanged(it)
                }
            )
        }
    )
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}