package com.application.mapa.feature.password.master

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.mapa.R
import com.application.mapa.feature.password.master.model.PasswordVerificationState
import com.application.mapa.feature.password.master.model.PasswordVerificationState.PasswordVerificationFailure
import com.application.mapa.feature.password.master.model.PasswordVerificationState.PasswordVerified
import com.application.mapa.ui.components.ErrorMessage
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
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                item {

                    if (state?.showWelcomeMessage == true) {
                        MasterPasswordWelcomeMessage()
                    }
                    MasterPasswordTextField(
                        state = masterPasswordFieldState,
                        onValueChanged = {
                            masterPasswordFieldState.value = masterPasswordFieldState.value.copy(
                                fieldValue = it
                            )
                        },
                        onDoneClicked = {
                            viewModel.verifyMasterPassword(
                                masterPasswordFieldState.value.fieldValue.text
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        viewModel.verifyMasterPassword(
                            masterPasswordFieldState.value.fieldValue.text
                        )
                    }) {
                        Text(text = getVerifyPasswordButtonText(state?.showWelcomeMessage))
                    }

                    if (state?.showRootError == true) {
                        Spacer(modifier = Modifier.height(8.dp))
                        ErrorMessage(
                            stringResource(R.string.root_risk_message),
                            stringResource(R.string.root_risk_message_description)
                        )
                    }

                    Spacer(modifier = Modifier.height(290.dp))
                }
            }
        }
    )
}

@Composable
private fun getVerifyPasswordButtonText(showWelcomeMessage: Boolean?): String {
    return if (showWelcomeMessage == true) {
        stringResource(R.string.create)
    } else {
        stringResource(R.string.unlock)
    }
}

@Composable
private fun MasterPasswordWelcomeMessage() {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .border(width = 1.dp, color = colorResource(R.color.gray), shape = RoundedCornerShape(6.dp))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.master_password_welcome_title),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.master_password_welcome_message),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }
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