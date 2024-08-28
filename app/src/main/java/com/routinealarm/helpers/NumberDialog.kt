package com.routinealarm.helpers

import android.util.Log
import android.widget.NumberPicker
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun NumberDialog(
    label : String = "",
    text : String = "0",
    numRows : Int = 3,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
)
{
    val num = text.toIntOrNull()
    if(numRows<1 || num==null) return

    var editText by rememberSaveable { mutableStateOf(text) }

    var max = 1 // 0-9, 0-99, 0-999, etc...
    for(i in 0..<numRows) max *= 10
    max -= 1

    DialogWrapper(
        modifier = Modifier.width(200.dp),
        onDismiss = onDismiss,
        onConfirm = {onConfirm(editText)}
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SimpleNumberPicker(
                value = num,
                min = 0,
                max = max,
                onValueChange = { editText = it.toString(); Log.i("NUM",editText)}
            )
        }
    }
}

@Composable
fun SimpleNumberPicker(
    value: Int,
    min: Int = 0,
    max: Int = 9,
    onValueChange: (Int) -> Unit
) {
    AndroidView(
        factory = { context ->
            NumberPicker(context).apply {
                setOnValueChangedListener { _, i, _ -> onValueChange(i) }
                minValue = min
                maxValue = max
                this.value = value
                textSize = 100F
                clipToOutline = true
            }
        },
        update = {}
    )
}