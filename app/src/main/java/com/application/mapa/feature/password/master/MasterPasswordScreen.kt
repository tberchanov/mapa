package com.application.mapa.feature.password.master

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.application.mapa.R
import com.application.mapa.feature.password.master.model.PasswordVerificationState
import com.application.mapa.feature.password.master.model.PasswordVerificationState.PasswordVerificationFailure
import com.application.mapa.feature.password.master.model.PasswordVerificationState.PasswordVerified
import com.application.mapa.util.Event

@Composable
fun MasterPasswordScreen(
    viewModel: MasterPasswordViewModel,
    navigateToPasswordList: () -> Unit
) {
    val masterPasswordFieldHint = stringResource(R.string.master_password_hint)
    val masterPasswordFieldState = remember {
        mutableStateOf(
            MasterPasswordTextFieldState(
                placeholder = masterPasswordFieldHint,
                fieldValue = TextFieldValue(),
                errorEnabled = false
            )
        )
    }

    val state by viewModel.state.observeAsState()

    ObservePasswordVerificationState(
        state?.verificationState,
        navigateToPasswordList,
        onVerificationFailure = {
            masterPasswordFieldState.value = masterPasswordFieldState.value.copy(
                errorEnabled = true
            )
        }
    )

    Scaffold(
        bodyContent = {
            ScrollableColumn(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                MasterPasswordTextField(
                    state = masterPasswordFieldState,
                    onValueChanged = {
                        masterPasswordFieldState.value = masterPasswordFieldState.value.copy(
                            fieldValue = it
                        )
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    viewModel.verifyMasterPassword(
                        masterPasswordFieldState.value.fieldValue.text
                    )
                }) {
                    Text(text = stringResource(R.string.unlock))
                }

                if (state?.showRootError == true) {
                    Spacer(modifier = Modifier.height(8.dp))
                    ErrorMessage(stringResource(R.string.root_risk_message))
                }

                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    )
}

@Composable
fun ErrorMessage(
    message: String
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        backgroundColor = MaterialTheme.colors.error
    ) {
        Text(modifier = Modifier.padding(8.dp), text = message)
    }
}

@Composable
fun ObservePasswordVerificationState(
    verificationState: Event<PasswordVerificationState>?,
    navigateToPasswordList: () -> Unit,
    onVerificationFailure: () -> Unit,
) {
    verificationState
        ?.getContentIfNotHandled()
        ?.let {
            when (it) {
                PasswordVerified -> navigateToPasswordList()
                PasswordVerificationFailure -> onVerificationFailure()
            }
        }
}