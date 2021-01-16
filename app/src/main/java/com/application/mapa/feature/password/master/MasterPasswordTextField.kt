package com.application.mapa.feature.password.master

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

data class MasterPasswordTextFieldState(
    val placeholder: String,
    val fieldValue: TextFieldValue,
    val errorEnabled: Boolean
)

@Composable
fun MasterPasswordTextField(
    state: State<MasterPasswordTextFieldState>,
    onValueChanged: (TextFieldValue) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 8.dp
            )
            .fillMaxWidth(),
        isErrorValue = state.value.errorEnabled,
        value = state.value.fieldValue,
        onValueChange = { onValueChanged(it) },
        label = { Text(state.value.placeholder) },
    )
}