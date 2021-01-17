package com.application.mapa.feature.settings.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.AndroidDialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import com.application.mapa.R
import com.application.mapa.ui.MapaTheme
import com.application.mapa.ui.PasswordTextField

@Composable
fun EnterPasswordDialog(
    onDialogConfirmed: (String) -> Unit,
    onDialogCancelled: () -> Unit,
) {
    val masterPasswordTextFieldState = mutableStateOf(TextFieldValue())

    AlertDialog(
        onDismissRequest = onDialogCancelled,
        title = {
            Text(
                text = stringResource(R.string.enter_master_password),
                color = MapaTheme.textColor
            )
        },
        text = {
            Column {
                // Empty text is needed for margin between title. Spacer doesn't work here/
                Text(modifier = Modifier.height(0.dp), text = "")
                PasswordTextField(state = masterPasswordTextFieldState)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.enter_password_dialog_description),
                    color = MapaTheme.textColor
                )
            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    ),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = onDialogCancelled) {
                    Text(text = stringResource(R.string.cancel))
                }
                Button(
                    onClick = { onDialogConfirmed(masterPasswordTextFieldState.value.text) }
                ) {
                    Text(text = stringResource(R.string.ok))
                }
            }
        },
        properties = AndroidDialogProperties(securePolicy = SecureFlagPolicy.SecureOn),
        backgroundColor = MapaTheme.dialogBackgroundColor
    )
}
