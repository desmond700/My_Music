package com.example.mymusic.ui.screens.settings

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SettingsSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Switch(
        modifier = Modifier
            .height(10.dp)
            .padding(0.dp),
        checked = checked,
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.Yellow,
            checkedTrackColor = Color.Magenta,
            uncheckedThumbColor = Color.White,
            uncheckedTrackColor = Color.Gray
        ),
        onCheckedChange = onCheckedChange
    )
}