package com.routinealarm.helpers

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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

    Surface(
    ) {
        DropdownMenu(
            modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer),
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
}