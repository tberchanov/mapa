package com.application.mapa.feature.password.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.loadVectorResource
import androidx.compose.ui.res.stringResource
import androidx.ui.tooling.preview.Preview
import com.application.mapa.R
import com.application.mapa.data.domain.model.Password
import com.application.mapa.feature.password.list.model.SelectablePassword

@Preview
@Composable
fun PasswordListScreenPreview() {
    PasswordListScreen(
        passwords = listOf(
            SelectablePassword(Password(1, "Name", "Value"), false),
            SelectablePassword(Password(2, "Name2", "Value2"), false),
            SelectablePassword(Password(3, "Name3", "Value3"), false)
        ),
        selectionEnabled = true
    )
}

@Composable
fun PasswordListScreen(
    passwords: List<SelectablePassword>,
    onCreatePasswordClick: () -> Unit = {},
    onDeletePasswordsClick: () -> Unit = {},
    onPasswordClick: (SelectablePassword) -> Unit = {},
    onPasswordLongClick: (SelectablePassword) -> Unit = {},
    onPasswordChecked: (SelectablePassword) -> Unit = {},
    onCloseClicked: () -> Unit = {},
    onSettingsClicked: () -> Unit = {},
    selectionEnabled: Boolean
) {
    Scaffold(
        topBar = {
            PasswordListTopBar(
                selectionEnabled,
                onCloseClicked,
                onSettingsClicked
            )
        },
        floatingActionButton = {
            PasswordListButton(selectionEnabled, onCreatePasswordClick, onDeletePasswordsClick)
        },
        bodyContent = {
            LazyColumn {
                items(passwords) { password ->
                    PasswordItem(
                        password,
                        selectionEnabled,
                        onPasswordClick,
                        onPasswordLongClick,
                        onPasswordChecked
                    )
                }
            }
        }
    )
}

@Composable
inline fun PasswordListTopBar(
    selectionEnabled: Boolean,
    crossinline onCloseClicked: () -> Unit = {},
    crossinline onSettingsClicked: () -> Unit = {},
) {
    TopAppBar(
        title = { Text(stringResource(R.string.passwords)) },
        actions = {
            if (selectionEnabled) {
                IconButton(onClick = {
                    onCloseClicked()
                }) {
                    Icon(imageVector = Icons.Default.Close)
                }
            } else {
                IconButton(onClick = {
                    onSettingsClicked()
                }) {
                    val imageRes = loadVectorResource(R.drawable.ic_settings)
                    imageRes.resource.resource?.let {
                        Image(imageVector = it)
                    }
                }
            }
        }
    )
}

@Composable
fun PasswordListButton(
    selectionEnabled: Boolean,
    onCreatePasswordClick: () -> Unit,
    onDeletePasswordsClick: () -> Unit
) {
    val imageRes = loadVectorResource(
        id = if (selectionEnabled) R.drawable.ic_delete else R.drawable.ic_add
    )
    FloatingActionButton(
        onClick = if (selectionEnabled) onDeletePasswordsClick else onCreatePasswordClick
    ) {
        imageRes.resource.resource?.let {
            Image(imageVector = it)
        }
    }
}