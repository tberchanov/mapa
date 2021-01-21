package com.application.mapa.feature.password.data

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.application.mapa.R
import com.application.mapa.data.domain.model.Password
import com.application.mapa.ui.BackIconButton
import com.application.mapa.ui.PasswordTextField
import com.application.mapa.ui.VectorIconButton

@Composable
fun PasswordDataScreen(
    passwordId: Long?,
    onSavePasswordClick: (Password) -> Unit,
    passwordDataViewModel: PasswordDataViewModel,
    navigateUp: () -> Unit,
    generatePassword: (String) -> Unit,
) {
    ObservePasswordSavingState(passwordDataViewModel, navigateUp)

    val password by passwordDataViewModel.state.observeAsState()
    remember(passwordId) {
        passwordDataViewModel.loadPassword(passwordId)
        true // stub, because remember cannot return Unit
    }
    val passwordName = mutableStateOf(TextFieldValue(password?.name ?: ""))
    val passwordValue = mutableStateOf(TextFieldValue(password?.value ?: ""))

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    BackIconButton(navigateUp)
                },
                actions = {
                    VectorIconButton(R.drawable.ic_check, onClick = {
                        onSavePasswordClick(
                            Password(
                                id = password?.id ?: Password.UNDEFINED_ID,
                                name = passwordName.value.text,
                                value = passwordValue.value.text,
                            )
                        )
                    })
                }
            )
        },
        bodyContent = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                NameTextField(stringResource(R.string.name), passwordName)
                Divider(
                    modifier = Modifier.preferredHeight(12.dp),
                    color = Color.Transparent
                )
                PasswordTextField(
                    stringResource(R.string.value),
                    passwordValue,
                    copyButtonVisible = true,
                    onCopyClicked = passwordDataViewModel::onCopyClicked
                )
                Divider(
                    modifier = Modifier.preferredHeight(12.dp),
                    color = Color.Transparent
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = {
                        generatePassword(passwordValue.value.text)
                    }) {
                        Text(text = stringResource(R.string.generate_password))
                    }
                }
            }
        }
    )
}

@Composable
fun NameTextField(hint: String, state: MutableState<TextFieldValue>) {
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

@Composable
private fun ObservePasswordSavingState(
    passwordDataViewModel: PasswordDataViewModel,
    navigateUp: () -> Unit
) {
    passwordDataViewModel.savingState
        .observeAsState()
        .value
        ?.getContentIfNotHandled()
        ?.let {
            if (it == PasswordDataState.SavingSuccess) {
                navigateUp()
            }
        }
}
