package com.application.mapa.feature.password.data

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.mapa.R
import com.application.mapa.feature.password.data.model.PasswordDataScreenAction.*
import com.application.mapa.ui.components.BackIconButton
import com.application.mapa.ui.components.ErrorText
import com.application.mapa.ui.components.PasswordTextField
import com.application.mapa.ui.components.VectorIconButton

@Composable
fun PasswordDataScreen(
    passwordId: Long?,
    viewModel: PasswordDataViewModel,
    navigateUp: () -> Unit,
    generatePassword: (String) -> Unit,
) {
    ObservePasswordSavingState(viewModel, navigateUp)

    val state by viewModel.state.observeAsState()

    DisposableEffect(
        key1 = state?.password?.id,
        effect = {
            viewModel.postAction(LoadPassword(passwordId))
            this.onDispose {
                viewModel.postAction(CleanData(passwordId))
            }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    BackIconButton(navigateUp)
                },
                actions = {
                    val checkButtonEnabled = state?.checkButtonEnabled ?: false
                    VectorIconButton(
                        if (checkButtonEnabled) R.drawable.ic_check else R.drawable.ic_check_disabled,
                        onClick = { viewModel.postAction(SavePassword) },
                        enabled = checkButtonEnabled
                    )
                }
            )
        },
        content = {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                val passwordFocusRequester = FocusRequester()
                NameTextField(
                    stringResource(R.string.name),
                    state?.password?.name ?: "",
                    onValueChanged = {
                        viewModel.postAction(ModifyPasswordName(it))
                    },
                    state?.showNameError == true,
                    onNextClicked = {
                        passwordFocusRequester.requestFocus()
                    }
                )
                if (state?.showNameError == true) {
                    ErrorText(
                        text = stringResource(R.string.name_should_not_be_empty),
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                PasswordTextField(
                    modifier = Modifier.focusRequester(passwordFocusRequester),
                    hint = stringResource(R.string.value),
                    text = state?.password?.value ?: "",
                    onValueChange = {
                        viewModel.postAction(ModifyPasswordValue(it))
                    },
                    copyButtonVisible = true,
                    onCopyClicked = {
                        viewModel.postAction(CopyPassword)
                    },
                    isErrorValue = state?.showValueError == true,
                    onDoneClicked = {
                        viewModel.postAction(SavePassword)
                    }
                )
                if (state?.showValueError == true) {
                    ErrorText(
                        text = stringResource(R.string.password_should_not_be_empty),
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = {
                        generatePassword(state?.password?.value ?: "")
                    }) {
                        Text(text = stringResource(R.string.generate_password))
                    }
                }
            }
        }
    )
}

@Composable
fun NameTextField(
    hint: String,
    text: String,
    onValueChanged: (String) -> Unit,
    isErrorValue: Boolean,
    onNextClicked: () -> Unit
) {
    Column {
        if (isErrorValue) {
            Text(hint, color = MaterialTheme.colors.error)
        } else {
            Text(hint)
        }
        val maxNameLength = integerResource(id = R.integer.max_field_length)
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = {
                if (it.length <= maxNameLength) {
                    onValueChanged(it)
                }
            },
            isError = isErrorValue,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { onNextClicked() })
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
