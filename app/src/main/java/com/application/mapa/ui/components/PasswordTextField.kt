package com.application.mapa.ui.components

import androidx.compose.foundation.Interaction
import androidx.compose.foundation.InteractionState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.application.mapa.R
import com.application.mapa.util.contains

private val blockCharacters = charArrayOf('\n')

@Composable
fun PasswordTextField(
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
            modifier = Modifier.fillMaxWidth(),
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
                            Icon(imageVector = vectorResource(id = R.drawable.ic_copy))
                        }
                    }
                    PasswordVisibilityIcon(passwordVisibility)
                }
            },
            isErrorValue = isErrorValue,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            onImeActionPerformed = { imeAction, _ ->
                if (imeAction == ImeAction.Done) {
                    onDoneClicked()
                }
            },
            interactionState = InteractionState().apply {
                addInteraction(Interaction.Focused)
                addInteraction(Interaction.Pressed)
            }
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