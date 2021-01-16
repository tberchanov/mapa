package com.application.mapa.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import com.application.mapa.R

@Composable
fun PasswordTextField(hint: String? = null, state: MutableState<TextFieldValue>) {
    val passwordVisibility = remember { mutableStateOf(false) }
    Column {
        if (hint != null) {
            Text(hint)
        }
        val textState = state
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = textState.value,
            onValueChange = { textState.value = it },
            visualTransformation = when (passwordVisibility.value) {
                true -> VisualTransformation.None
                false -> PasswordVisualTransformation()
            },
            trailingIcon= { PasswordVisibilityIcon(passwordVisibility) }
        )
    }
}

@Composable
fun PasswordVisibilityIcon(passwordVisibility: MutableState<Boolean>) {
    IconButton(onClick = {
        passwordVisibility.value = !passwordVisibility.value
    }) {
        val iconId = when (passwordVisibility.value) {
            true -> R.drawable.ic_visibility
            false -> R.drawable.ic_visibility_off
        }
        Icon(imageVector = vectorResource(id = iconId))
    }
}