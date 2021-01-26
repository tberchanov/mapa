package com.application.mapa.feature.password.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.loadVectorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.application.mapa.R
import com.application.mapa.feature.password.list.model.SelectablePassword
import com.application.mapa.ui.components.ErrorMessage

@Composable
fun PasswordListScreen(
    viewModel: PasswordListViewModel,
    onCreatePasswordClick: () -> Unit = {},
    onSettingsClicked: () -> Unit = {},
    passwordDetails: (Long) -> Unit
) {
    val state by viewModel.state.observeAsState()
    val selectionEnabled = state?.selectionEnabled ?: false
    Scaffold(
        topBar = {
            PasswordListTopBar(
                selectionEnabled,
                onCloseClicked = { viewModel.disableSelection() },
                onSettingsClicked
            )
        },
        floatingActionButton = {
            PasswordListButton(
                selectionEnabled,
                onCreatePasswordClick,
                onDeletePasswordsClick = { viewModel.deleteSelectedPasswords() }
            )
        },
        bodyContent = {
            state?.run {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (showRootError) {
                        Spacer(modifier = Modifier.height(8.dp))
                        ErrorMessage(
                            stringResource(R.string.root_risk_message),
                            stringResource(R.string.root_risk_message_description)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    PasswordsList(
                        passwords,
                        selectionEnabled,
                        onPasswordSelected = viewModel::selectPassword,
                        passwordDetails = passwordDetails,
                    )
                }
            }
        }
    )
}

@Composable
private fun PasswordsList(
    passwords: List<SelectablePassword>,
    selectionEnabled: Boolean,
    onPasswordSelected: (SelectablePassword) -> Unit,
    passwordDetails: (Long) -> Unit
) {
    LazyColumn {
        items(passwords) { password ->
            PasswordItem(
                password,
                selectionEnabled,
                onPasswordClick = {
                    if (selectionEnabled) {
                        onPasswordSelected(it)
                    } else {
                        passwordDetails(it.password.id)
                    }
                },
                onPasswordLongClick = {
                    onPasswordSelected(it)
                },
                onPasswordChecked = {
                    onPasswordSelected(it)
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
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