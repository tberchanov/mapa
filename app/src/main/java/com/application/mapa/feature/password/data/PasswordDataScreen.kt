package com.application.mapa.feature.password.data

import android.util.Log
import androidx.compose.material.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.application.mapa.data.domain.model.Password

@Composable
fun PasswordDataScreen(
    passwordId: Long?,
    onSavePasswordClick: (Password) -> Unit,
    passwordDataViewModel: PasswordDataViewModel
) {
    val password by passwordDataViewModel.getPassword(passwordId).observeAsState()
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        val passwordName = mutableStateOf(TextFieldValue(password?.name ?: ""))
        val passwordValue = mutableStateOf(TextFieldValue(password?.value ?: ""))
        PasswordTextField("Name", passwordName)
        Divider(
            modifier = Modifier.preferredHeight(12.dp),
            color = Color.Transparent
        )
        PasswordTextField("Value", passwordValue)
        Divider(
            modifier = Modifier.preferredHeight(12.dp),
            color = Color.Transparent
        )
        SavePasswordButton(
            onSavePasswordClick,
            passwordId = password?.id ?: Password.UNDEFINED_ID,
            passwordNameState = passwordName,
            passwordValueState = passwordValue
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

/*
@Preview
@Composable
fun PasswordDataScreenPreview() {
    PasswordDataScreen(password = Password(1, "Gmail", "Qwert_123"), {})
}*/
