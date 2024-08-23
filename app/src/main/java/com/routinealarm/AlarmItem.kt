package com.routinealarm

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AlarmItem (
    alarm: Alarm,
    enabled: Boolean,
    checked: Boolean,
    onEnabled: (Boolean) -> Unit,
    onChecked: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isEditMode: Boolean = false
){
    Row(
        modifier = modifier.padding(horizontal = 20.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val color : Color = if (enabled) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.inversePrimary

        Column (modifier = modifier
            .weight(1f)
            .padding(vertical = 5.dp)
        ) {
            Text(
                text = alarm.label,
                style = MaterialTheme.typography.bodyLarge,
                color = color
            )
            Text(
                modifier = modifier.padding(horizontal = 5.dp),
                text = if (alarm.timeInterval.toInt()>0) "%s every %s min".format(alarm.timeStart,alarm.timeInterval)
                else "%s no repeat".format(alarm.timeStart),
                style = MaterialTheme.typography.bodySmall,
                color = color
            )
        }

        if (isEditMode) {
            Checkbox(
                checked = checked,
                onCheckedChange = onChecked
            )
        }
        else {
            Switch(
                modifier = Modifier,
                checked = enabled,
                onCheckedChange = onEnabled
            )
        }
    }
}
