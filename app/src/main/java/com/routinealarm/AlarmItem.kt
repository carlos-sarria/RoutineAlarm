package com.routinealarm

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.routinealarm.ui.theme.cBackground
import com.routinealarm.ui.theme.cBackgroundDialog
import com.routinealarm.ui.theme.cText
import com.routinealarm.ui.theme.cTextDisabled
import com.routinealarm.ui.theme.cTextVariant
import com.routinealarm.ui.theme.cThumb

@Composable
fun textColor(enabled : Boolean, color : Color = MaterialTheme.colorScheme.cText) : Color {
    return if (enabled) color
    else MaterialTheme.colorScheme.cTextDisabled
}

@Composable
fun ExpandableSection(
    modifier: Modifier = Modifier,
    forceExpanded : Boolean = false,
    enabled: Boolean = true,
    contentTitle: @Composable () -> Unit,
    contentExpanded: @Composable () -> Unit
) {
    var isExpanded by remember { mutableStateOf(forceExpanded) }
    val icon = if (isExpanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown

    Column(
        modifier = modifier
            .clickable { isExpanded = !isExpanded }
            .background(color = MaterialTheme.colorScheme.cBackground)
            .fillMaxWidth()
    ) {
         Row(modifier = modifier.clickable{isExpanded=!isExpanded},
            verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier.size(32.dp),
                imageVector = icon,
                colorFilter = ColorFilter.tint(color = textColor(enabled)),
                contentDescription = "collapsable"
            )
            contentTitle()
        }

        AnimatedVisibility(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.cBackgroundDialog)
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
    onEnableChanged: (Boolean) -> Unit,
    onDeleted: () -> Unit,
    onUpdated: () -> Unit
) {

    ExpandableSection(
        modifier = Modifier,
        forceExpanded = forceExpanded,
        enabled = alarm.enabled,
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
                        color = textColor(alarm.enabled)
                    )
                    Text(
                        modifier = modifier.padding(horizontal = 5.dp),
                        text = if (alarm.timeInterval.toInt() > 0) "%s every %s min".format(
                            alarm.timeStart,
                            alarm.timeInterval.replaceFirst (regex = Regex("^0+"), "")
                        )
                        else "%s no repeat".format(alarm.timeStart),
                        style = MaterialTheme.typography.bodySmall,
                        color = textColor(alarm.enabled)
                    )
                }
                Switch(
                    modifier = Modifier.padding(end = 10.dp),
                    checked = alarm.enabled,
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = MaterialTheme.colorScheme.cTextVariant,
                        checkedThumbColor = MaterialTheme.colorScheme.cThumb
                    ),
                    onCheckedChange = onEnableChanged
                )
            }
        },
        contentExpanded = {
            AlarmSetup (
                modifier = Modifier,
                alarm = alarm,
                enabled = alarm.enabled,
                onDelete = onDeleted,
                onUpdated = onUpdated
            )
        }
    )
}