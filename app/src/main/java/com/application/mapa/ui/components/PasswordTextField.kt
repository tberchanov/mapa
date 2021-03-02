package com.application.mapa.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.application.mapa.R
import com.application.mapa.util.contains

private val blockCharacters = charArrayOf('\n')

@Composable
fun PasswordTextField(
    modifier: Modifier,
    text: String,
    onValueChange: (String) -> Unit,
    hint: String? = null,
    copyButtonVisible: Boolean = false,
    onCopyClicked: (String) -> Unit = {},
    isErrorValue: Boolean = false,
    onDoneClicked: () -> Unit
) {
    val passwordVisibility = remember { mutableStateOf(false) }
    Column {
        if (hint != null) {
            if (isErrorValue) {
                ErrorText(hint)
            } else {
                Text(hint)
            }
        }
        TextField(
            modifier = modifier.fillMaxWidth(),
            value = text,
            onValueChange = {
                if (!it.contains(blockCharacters)) {
                    onValueChange(it)
                }
            },
            visualTransformation = when (passwordVisibility.value) {
                true -> VisualTransformation.None
                false -> PasswordVisualTransformation()
            },
            trailingIcon = {
                Row {
                    if (copyButtonVisible) {
                        IconButton(onClick = {
                            onCopyClicked(text)
                        }) {
                            Icon(painter = painterResource(id = R.drawable.ic_copy), contentDescription = null)
                        }
                    }
                    PasswordVisibilityIcon(passwordVisibility)
                }
            },
            isError = isErrorValue,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onDoneClicked() }),
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
        Icon(painter = painterResource(id = iconId), contentDescription = null)
    }
}