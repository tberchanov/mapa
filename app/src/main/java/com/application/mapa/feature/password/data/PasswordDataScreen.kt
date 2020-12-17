package com.application.mapa.feature.password.data

import android.util.Log
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.application.mapa.data.domain.model.Password

@Composable
fun PasswordDataScreen(
    password: Password?, onSavePasswordClick: (Password) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        val passwordNameState = remember {
            mutableStateOf(TextFieldValue(password?.name ?: ""))
        }
        val passwordValueState = remember {
            mutableStateOf(TextFieldValue(password?.value ?: ""))
        }
        PasswordTextField("Name", passwordNameState)
        Divider(
            modifier = Modifier.preferredHeight(12.dp),
            color = Color.Transparent
        )
        PasswordTextField("Value", passwordValueState)
        Divider(
            modifier = Modifier.preferredHeight(12.dp),
            color = Color.Transparent
        )
        SavePasswordButton(
            onSavePasswordClick,
            passwordId = password?.id ?: Password.UNDEFINED_ID,
            passwordNameState = passwordNameState,
            passwordValueState = passwordValueState
        )
    }
}

@Composable
fun SavePasswordButton(
    onSavePasswordClick: (Password) -> Unit,
    passwordId: Long,
    passwordNameState: State<TextFieldValue>,
    passwordValueState: State<TextFieldValue>,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.weight(1.0f))
        TextButton(onClick = {
            onSavePasswordClick(
                Password(
                    id = passwordId,
                    name = passwordNameState.value.text,
                    value = passwordValueState.value.text,
                )
            )
        }) {
            Text(text = "Save")
        }
    }
}

@Composable
fun PasswordTextField(hint: String, state: MutableState<TextFieldValue>) {
    Log.e("PasswordDataScreen", "PasswordTextField recomposed") // TODO
    Column {
        Text(hint)
        val textState = state
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = textState.value,
            onValueChange = { textState.value = it },
        )
    }
}

@Preview
@Composable
fun PasswordDataScreenPreview() {
    PasswordDataScreen(password = Password(1, "Gmail", "Qwert_123"), {})
}