package com.application.mapa.feature.password.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.loadVectorResource
import androidx.compose.ui.res.stringResource
import com.application.mapa.R

@Composable
fun PasswordListScreen(
    viewModel: PasswordListViewModel,
    onCreatePasswordClick: () -> Unit = {},
    onSettingsClicked: () -> Unit = {},
    passwordDetails: (Long) -> Unit
) {
    val selectionEnabled = viewModel.state.selectionEnabled
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
            LazyColumn {
                items(viewModel.state.passwords) { password ->
                    PasswordItem(
                        password,
                        selectionEnabled,
                        onPasswordClick = {
                            if (viewModel.state.selectionEnabled) {
                                viewModel.selectPassword(it)
                            } else {
                                passwordDetails(it.password.id)
                            }
                        },
                        onPasswordLongClick = {
                            viewModel.selectPassword(it)
                        },
                        onPasswordChecked = {
                            viewModel.selectPassword(it)
                        }
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