package com.application.mapa.feature.password.master

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.application.mapa.R
import com.application.mapa.feature.password.master.PasswordVerificationState.PasswordVerificationFailure
import com.application.mapa.feature.password.master.PasswordVerificationState.PasswordVerified

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

    ObservePasswordVerificationState(
        viewModel,
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
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    )
}

@Composable
fun ObservePasswordVerificationState(
    viewModel: MasterPasswordViewModel,
    navigateToPasswordList: () -> Unit,
    onVerificationFailure: () -> Unit,
) {
    viewModel.verificationState
        .observeAsState()
        .value
        ?.getContentIfNotHandled()
        ?.let {
            when (it) {
                PasswordVerified -> navigateToPasswordList()
                PasswordVerificationFailure -> onVerificationFailure()
            }
        }
}