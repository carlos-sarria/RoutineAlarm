package com.routinealarm.helpers

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun ComboDialog(
    list: Array<String>,
    selected : String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
)
{
    var selectedText by remember { mutableStateOf(selected) }
    var expanded by remember { mutableStateOf(true) }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false; onDismiss() }
    )
    {
        list.forEach { item ->
            DropdownMenuItem(
                text = { Text(text = item) },
                onClick = {
                    selectedText = item
                    onConfirm(selectedText)
                }
            )
        }
    }
}