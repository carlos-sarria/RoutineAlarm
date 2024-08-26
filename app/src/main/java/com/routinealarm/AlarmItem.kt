package com.routinealarm

import android.view.View
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun ExpandableSection(
    modifier: Modifier = Modifier,
    forceExpanded : Boolean = false,
    contentTitle: @Composable () -> Unit,
    contentExpanded: @Composable () -> Unit
) {
    var isExpanded by remember { mutableStateOf(forceExpanded) }
    val icon = if (isExpanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown

    Column(
        modifier = modifier
            .clickable { isExpanded = !isExpanded }
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
    ) {
         Row(modifier = modifier.clickable{isExpanded=!isExpanded},
            verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier.size(32.dp),
                imageVector = icon,
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimaryContainer),
                contentDescription = "collapsable"
            )
            contentTitle()
        }

        AnimatedVisibility(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .fillMaxWidth(),
            visible = isExpanded
        ) {
            contentExpanded()
        }
    }
}
@Composable
fun AlarmItem (
    modifier: Modifier = Modifier,
    alarm: Alarm,
    forceExpanded : Boolean = false,
    enabled: Boolean,
    onEnabled: (Boolean) -> Unit,
    onDeleted: () -> Unit,
    model : ViewModel
) {
    val color : Color = if (enabled) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.inversePrimary

    ExpandableSection(
        modifier = Modifier,
        forceExpanded = forceExpanded,
        contentTitle = {
            Row(
                modifier = modifier.padding(vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = modifier
                    .padding(start = 10.dp)
                    .weight(1f))
                {
                    Text(
                        modifier = modifier.padding(horizontal = 5.dp),
                        text = alarm.label,
                        style = MaterialTheme.typography.titleLarge,
                        color = color
                    )
                    Text(
                        modifier = modifier.padding(horizontal = 5.dp),
                        text = if (alarm.timeInterval.toInt() > 0) "%s every %s min".format(
                            alarm.timeStart,
                            alarm.timeInterval
                        )
                        else "%s no repeat".format(alarm.timeStart),
                        style = MaterialTheme.typography.bodySmall,
                        color = color
                    )
                }
                Switch(
                    modifier = Modifier.padding(end = 10.dp),
                    checked = enabled,
                    onCheckedChange = onEnabled
                )
            }
        },
        contentExpanded = {
            AlarmSetup (
                modifier = Modifier,
                model = model,
                alarmId = 0,
                onDelete = onDeleted
            )
        }
    )
}