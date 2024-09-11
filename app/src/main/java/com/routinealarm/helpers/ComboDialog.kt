package com.routinealarm.helpers

import androidx.compose.foundation.background
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.routinealarm.ui.theme.cBackgroundDialog
import com.routinealarm.ui.theme.cTextVariant

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

    Surface {
        DropdownMenu(
            modifier = Modifier.background(cBackgroundDialog),
            expanded = expanded,
            onDismissRequest = { expanded = false; onDismiss() }
        )
        {
            list.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                color = cTextVariant,
                                text = item
                            )
                        },
                        onClick = {
                            selectedText = item
                            SoundManager.play(item, 1)
                            onConfirm(selectedText)
                        }
                    )
            }
        }
    }
}