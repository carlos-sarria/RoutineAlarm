package com.routinealarm

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
                text = "%s to %s every %s min".format(
                    alarm.timeStart,
                    alarm.timeEnd,
                    alarm.timeInterval
                ),
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AlarmList(
    modifier: Modifier = Modifier,
    model: ViewModel,
    onListButtonClicked: (Int) -> Unit,
    alarmList : List<Alarm>  //= listOf<Alarm>(), // empty list as initializer
){
    var isEdited by rememberSaveable { mutableStateOf(false) }
    var allChecked by rememberSaveable { mutableStateOf(false) }
    val scope = currentRecomposeScope

    Scaffold( // Scaffold give us the easiest floating button
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape,
                onClick = {
                    if(isEdited) model.deleteChecked()
                    else onListButtonClicked(-1)
                    scope.invalidate() },
                modifier = Modifier,
            ) {
                if(isEdited)  Icon(Icons.Outlined.Delete, null)
                else          Icon(Icons.Outlined.Add, null)
            }
        }
    ) {
        Column(modifier = modifier.padding(start = 10.dp, top = 50.dp, end = 10.dp, bottom = 50.dp))
        {
            // Top Row. TODO: Move it to scaffold
            Row(
                modifier = modifier
                    .padding(horizontal = 20.dp, vertical = 5.dp)
                    .height(32.dp),
                verticalAlignment = Alignment.CenterVertically
            )
            {

                if (isEdited) {
                    Icon(Icons.Outlined.Close,
                        contentDescription = "Close",
                        modifier = modifier
                            .padding(horizontal = 5.dp)
                            .clickable { isEdited = !isEdited }
                    )

                    Spacer(modifier = modifier.weight(1f))

                    Checkbox(
                        checked = allChecked,
                        onCheckedChange = {
                            allChecked = !allChecked
                            model.alarms.forEach { alarm ->
                                alarm.checked = allChecked
                            }
                        }
                    )

                } else {
                    Text(
                        modifier = modifier
                            .padding(horizontal = 5.dp)
                            .clickable { isEdited = !isEdited },
                        text = "Edit",
                        style = MaterialTheme.typography.labelSmall,
                    )

                    Spacer(modifier = modifier.weight(1f))

                    Icon(Icons.Outlined.MoreVert,
                        contentDescription = "Configuration",
                        modifier = modifier.clickable { })
                }
            }

            // List of alarms
            LazyColumn(
                modifier = modifier
            ) {
                items(
                    items = alarmList,
                    key = { alarm: Alarm -> alarm.id }
                ) { alarm: Alarm ->
                    AlarmItem(
                        alarm = alarm,
                        enabled = alarm.enabled,
                        checked = alarm.checked,
                        onEnabled = { enabled -> model.changeAlarmEnabled(alarm, enabled) },
                        onChecked = { checked -> model.changeAlarmChecked(alarm, checked) },
                        modifier = modifier.clickable { onListButtonClicked(alarm.id) },
                        isEditMode = isEdited
                    )
                }
            }
        }
    }
}