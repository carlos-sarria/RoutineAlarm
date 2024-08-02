package com.routinealarm.helpers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultipleSelection (
    title: String = "",
    listNames: Array<String>,
    listStates: Array<Boolean>,
    onChange: (Array<Boolean>) -> Unit
){
    // Convert the list of booleans into a string to be displayed on the Text
    var initialText  = ""
    listNames.forEachIndexed { index, item ->
        initialText += if (listStates[index]) item.first().uppercase()+" " else "- "}


    var openDialog by remember { mutableStateOf(false) }

    // ExposedDropdownMenuBox give us a clickable box for
    // TextField (which we use just for the style)
    ExposedDropdownMenuBox(expanded = false, onExpandedChange = {openDialog = true})
    {
        OutlinedTextField(
            value = initialText,
            onValueChange = {},
            readOnly = true,
            label = { Text(title) },
            trailingIcon = { },
            modifier = Modifier.menuAnchor()
        )
    }

    if (openDialog) {
        MultipleSelectionPopup(
            listNames = listNames,
            listStates = listStates,
            onConfirm = {
                newListStates ->  newListStates.copyInto(listStates)
                onChange(listStates)
                openDialog = false },
            onDismiss = { openDialog = false }
        )
    }
}

@Composable
fun MultipleSelectionPopup(
    listNames: Array<String>,
    listStates: Array<Boolean>,
    onDismiss: () -> Unit,
    onConfirm: (Array<Boolean>) -> Unit,
) {
    val tempList by remember { mutableStateOf(listStates.copyOf())  }
    val scope = currentRecomposeScope


    DialogWrapper( // From TimeSelect
        onDismiss = onDismiss,
        onConfirm = { onConfirm(tempList) }
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start)
        {
            listNames.forEachIndexed { index, item ->
                Row(verticalAlignment = Alignment.CenterVertically)
                {
                    Checkbox(
                        checked = tempList[index],
                        onCheckedChange = {  checked ->
                            // tempList += false; // Trick to force redraw
                            scope.invalidate()
                            tempList[index] = checked }
                    )
                    Text(text = item)
                }
            }
        }
    }
}