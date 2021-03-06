package com.application.mapa.feature.password.master

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.application.mapa.R
import com.application.mapa.ui.components.PasswordVisibilityIcon
import com.application.mapa.util.contains

private val blockCharacters = charArrayOf('\n')

data class MasterPasswordTextFieldState(
    val placeholder: String,
    val fieldValue: TextFieldValue,
    val errorEnabled: Boolean
)

@Composable
fun MasterPasswordTextField(
    state: State<MasterPasswordTextFieldState>,
    onValueChanged: (TextFieldValue) -> Unit,
    onDoneClicked: () -> Unit
) {
    val maxPasswordLength = integerResource(id = R.integer.max_field_length)
    val passwordVisibility = remember { mutableStateOf(false) }
    OutlinedTextField(
        modifier = Modifier
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 8.dp
            )
            .fillMaxWidth(),
        isError = state.value.errorEnabled,
        value = state.value.fieldValue,
        onValueChange = {
            if (!it.text.contains(blockCharacters) && it.text.length <= maxPasswordLength) {
                onValueChanged(it)
            }
        },
        label = { Text(state.value.placeholder) },
        visualTransformation = when (passwordVisibility.value) {
            true -> VisualTransformation.None
            false -> PasswordVisualTransformation()
        },
        trailingIcon = { PasswordVisibilityIcon(passwordVisibility) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { onDoneClicked() })
    )
}