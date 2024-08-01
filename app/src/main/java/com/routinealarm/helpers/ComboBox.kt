package com.routinealarm

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun ComboBox(
    title : String = "",
    list: Array<String>,
    onChange: (String) -> Unit)
{
    Row()
    {
        PullDown(list, title = title, onChange = { selection : String -> onChange(selection)} )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullDown(
    list: Array<String>,
    onChange: (String) -> Unit,
    title : String,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(list[0]) }

    Box() {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            OutlinedTextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                label = { Text(title) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                list.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            selectedText = item
                            expanded = false
                            onChange(selectedText)
                        }
                    )
                }
            }
        }
    }
}