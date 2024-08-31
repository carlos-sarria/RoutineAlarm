package com.routinealarm.helpers

import android.util.Log
import android.widget.NumberPicker
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.routinealarm.ui.theme.cTextVariant

@Composable
fun NumberDialog(
    text : String = "0",
    numRows : Int = 3,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
)
{
    if(numRows<1) return

    var n by remember { mutableStateOf(arrayOf<Int>()) }
    var exp  = 1
    for (i in 0..<numRows) {
        n += (text.toInt()/exp)%10 // separate each digit into the array
        exp *= 10
    }

    DialogWrapper(
        modifier = Modifier.width(250.dp),
        onDismiss = onDismiss,
        onConfirm = {
            var s  = ""
            for (i in numRows-1 downTo 0) { s += n[i].toString() } // recompose each digit into final string
            onConfirm(s)
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in (numRows-1) downTo 0) {
                    SimpleNumberPicker(
                        value = n[i],
                        onValueChange = { n[i] = it; }
                    )
                }
            }
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
    val color = MaterialTheme.colorScheme.cTextVariant.toArgb()

    AndroidView(
        modifier = Modifier.width(20.dp),
        factory = { context ->
            NumberPicker(context).apply {
                setOnValueChangedListener { _, _, i -> onValueChange(i) }
                minValue = min
                maxValue = max
                this.value = value
                textSize = 100F
                clipToOutline = true
                textColor = color
            }
        },
        update = {}
    )
}
