package com.routinealarm

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AlarmList(
    modifier: Modifier = Modifier,
    model: ViewModel,
    onListButtonClicked: (Int) -> Unit,
    alarmList : List<Alarm>
){
    var isEdited by rememberSaveable { mutableStateOf(false) }
    val scope = currentRecomposeScope
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.shadow(elevation = 8.dp),
                title = { Text(text = "Routine Alarm") },
                actions = {
                    IconButton(onClick = { isEdited = !isEdited }) {
                        Icon(imageVector = if(isEdited) Icons.Filled.Notifications else Icons.Filled.Edit, contentDescription = "Edit")
                    }
                    // over flow menu
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(text = { Text(text = "Clear all") }, onClick = {
                            model.deleteChecked(forceAll = true)
                            scope.invalidate()
                        })
                    }
                }
            )
        },
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

    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
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
