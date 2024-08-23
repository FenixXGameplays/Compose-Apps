package com.example.noteapp.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction

@Composable
fun NoteInputText(
    modifier: Modifier = Modifier,
    text: String,
    label: String,
    maxLine: Int = 1,
    onTextChange: (String) -> Unit,
    onImeAction: () -> Unit = {}
){

    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = text,
        label = { Text(text = label)},
        onValueChange = onTextChange,
        colors = TextFieldDefaults.colors(),
        maxLines = maxLine,
        modifier = modifier,
       // onImeAction = onImeAction,
//        keyboardOptions = KeyboardOptions.Default.copy(
//            imeAction = ImeAction.Done
//        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            onImeAction()
            keyboardController!!.hide()
        })

    )
}


@Composable
fun NoteButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
){
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier){
        Text(text = text)

    }
}