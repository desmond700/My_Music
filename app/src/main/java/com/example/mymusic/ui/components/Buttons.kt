package com.example.mymusic.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.mymusic.utils.MenuButtonItem
import com.example.mymusic.utils.MenuButtonItemDivider
import com.example.mymusic.utils.MenuButtonListItem

@Composable
fun MenuButton(
    modifier: Modifier = Modifier,
    button: @Composable (() -> Unit)? = null,
    click: () -> Unit,
    items: List<MenuButtonItem>
){
    var menuExpanded by remember { mutableStateOf(false) }
    Log.d("MenuButton", "menuExpanded $menuExpanded")

    Box {
        if (button == null) {
            IconButton(
                modifier = modifier,
                onClick = {
                    click()
                    menuExpanded = true
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = null
                )
            }
        }
        else {
            button()
        }
        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false },
        ) {
            items.forEach { item ->
                if (item is MenuButtonListItem) {
                    DropdownMenuItem(
                        text = {
                            Text(item.title)
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = item.icon.imageVector,
                                tint = item.icon.color,
                                contentDescription = item.icon.contentDescription
                            )
                        },
                        onClick = item.click
                    )
                }
                else if (item is MenuButtonItemDivider) {
                    item.divider()
                }
            }
        }
    }
}