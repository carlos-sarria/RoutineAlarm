package com.routinealarm.helpers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.routinealarm.textColor
import com.routinealarm.ui.theme.cBackground
import com.routinealarm.ui.theme.cTextVariant

enum class EditType {
    NONE, TEXT, NUMERIC, TIME, COMBO
}

@Composable
fun EditBox(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    label: String = "",
    initialText: String = "",
    type: EditType = EditType.NONE,
    list: Array<String> = arrayOf(""),
    numRows: Int = 3,
    enabled : Boolean = true,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var openDialog by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(initialText) }

    Row(modifier = modifier.clickable {
        if(type == EditType.NONE) onConfirm("")
        else openDialog = true })
    {
        if (icon != null) {
            Image(
                modifier = modifier.size(24.dp),
                imageVector = icon,
                colorFilter = ColorFilter.tint(textColor(enabled)),
                contentDescription = "collapsable"
            )
        }

        Text(
            modifier = modifier.padding(horizontal = 5.dp),
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor(enabled)
        )
        if(initialText != "") {
            Text(
                modifier = modifier
                    .background(MaterialTheme.colorScheme.cBackground)
                    .padding(horizontal = 5.dp),
                text = text,
                color = textColor(enabled, MaterialTheme.colorScheme.cTextVariant),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

    if (openDialog) {
        if (type == EditType.TEXT) {
            TextDialog(label, text, false,
                { openDialog = false; onDismiss() },
                { openDialog = false; text = it; onConfirm(text) }
            )
        }
        if (type == EditType.NUMERIC) {
            NumberDialog(text, numRows,
                { openDialog = false; onDismiss() },
                { openDialog = false; text = it; onConfirm(text) }
            )
        }
        if (type == EditType.TIME) {
            TimeDialog(text,
                { openDialog = false; onDismiss() },
                { openDialog = false; text = it; onConfirm(text) }
            )
        }
        if (type == EditType.COMBO) {
            ComboDialog(
                list, text,
                { openDialog = false; onDismiss() },
                { openDialog = false; text = it; onConfirm(text) }
            )
        }
    }
}