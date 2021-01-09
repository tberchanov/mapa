package com.application.mapa.feature.password.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.loadVectorResource
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
    selectionEnabled: Boolean
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Passwords" /*TODO move to resources*/) },
                actions = {
                    if (selectionEnabled) {
                        IconButton(onClick = {
                            onCloseClicked()
                        }) {
                            Icon(imageVector = Icons.Default.Close)
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            PasswordListButton(selectionEnabled, onCreatePasswordClick, onDeletePasswordsClick)
        },
        bodyContent = {
            LazyColumnFor(items = passwords) { password ->
                PasswordItem(
                    password,
                    selectionEnabled,
                    onPasswordClick,
                    onPasswordLongClick,
                    onPasswordChecked
                )
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