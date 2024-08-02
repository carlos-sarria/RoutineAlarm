package com.routinealarm.helpers

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.text.isDigitsOnly

@Composable
fun LineEdit (
    title : String = "",
    initialText : String = "",
    isNumeric : Boolean = false,
    maximum : Int = 0,
    onChange : (String) -> Unit
){
    var text by rememberSaveable { mutableStateOf(initialText) }
    val fullTitle : String = if (maximum > 0 && isNumeric) "$title (max $maximum)" else title

    OutlinedTextField(
        value = text,
        singleLine = true,
        onValueChange = {
            if (!isNumeric || it == "") { text = it; onChange(text) }
            else if (it.isDigitsOnly()){
                text = it
                if(text.toInt() > maximum && maximum>0) text = maximum.toString()
                onChange(text)}
        },
        label = { Text(fullTitle) },
        keyboardOptions = KeyboardOptions (
            keyboardType = if(isNumeric) KeyboardType.Number else KeyboardType.Text
        )
    )
}
