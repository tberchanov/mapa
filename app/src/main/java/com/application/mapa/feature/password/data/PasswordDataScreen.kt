package com.application.mapa.feature.password.data

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.mapa.R
import com.application.mapa.feature.password.data.model.PasswordDataScreenAction.*
import com.application.mapa.ui.BackIconButton
import com.application.mapa.ui.ErrorText
import com.application.mapa.ui.PasswordTextField
import com.application.mapa.ui.VectorIconButton

@Composable
fun PasswordDataScreen(
    passwordId: Long?,
    viewModel: PasswordDataViewModel,
    navigateUp: () -> Unit,
    generatePassword: (String) -> Unit,
) {
    ObservePasswordSavingState(viewModel, navigateUp)

    val state by viewModel.state.observeAsState()
    remember(passwordId) {
        viewModel.postAction(LoadPassword(passwordId))
        true // stub, because remember cannot return Unit
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    BackIconButton(navigateUp)
                },
                actions = {
                    VectorIconButton(R.drawable.ic_check, onClick = {
                        viewModel.postAction(SavePassword)
                    })
                }
            )
        },
        bodyContent = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                NameTextField(
                    stringResource(R.string.name),
                    state?.password?.name ?: "",
                    onValueChanged = {
                        viewModel.postAction(ModifyPasswordName(it))
                    },
                    state?.showNameError == true
                )
                if (state?.showNameError == true) {
                    ErrorText(
                        text = stringResource(R.string.name_should_not_be_empty),
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.preferredHeight(12.dp))
                PasswordTextField(
                    hint = stringResource(R.string.value),
                    text = state?.password?.value ?: "",
                    onValueChange = {
                        viewModel.postAction(ModifyPasswordValue(it))
                    },
                    copyButtonVisible = true,
                    onCopyClicked = {
                        viewModel.postAction(CopyPassword)
                    },
                    isErrorValue = state?.showValueError == true
                )
                if (state?.showValueError == true) {
                    ErrorText(
                        text = stringResource(R.string.password_should_not_be_empty),
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.preferredHeight(12.dp))
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
    isErrorValue: Boolean
) {
    Column {
        if (isErrorValue) {
            Text(hint, color = MaterialTheme.colors.error)
        } else {
            Text(hint)
        }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = onValueChanged,
            isErrorValue = isErrorValue
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
