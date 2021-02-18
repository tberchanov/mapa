package com.application.mapa.feature.settings

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import com.application.mapa.R
import com.application.mapa.feature.settings.model.SettingsAction.EnterPasswordDialogCancel
import com.application.mapa.feature.settings.model.SettingsAction.EnterPasswordDialogConfirm
import com.application.mapa.feature.settings.ui.EnterPasswordDialog
import com.application.mapa.feature.settings.ui.SettingItem
import com.application.mapa.ui.components.BackIconButton

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBackClicked: () -> Unit,
) {
    val state by viewModel.state.observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.settings))
                },
                navigationIcon = {
                    BackIconButton(onBackClicked)
                }
            )
        },
        bodyContent = {
            LazyColumn {
                state?.settingsList?.let { settingsList ->
                    items(settingsList) {
                        SettingItem(it, viewModel::postAction)
                    }
                }
            }
        }
    )

    if (state?.showEnterPasswordDialog == true) {
        EnterPasswordDialog(
            onDialogConfirmed = { viewModel.postAction(EnterPasswordDialogConfirm(it)) },
            onDialogCancelled = { viewModel.postAction(EnterPasswordDialogCancel) }
        )
    }
}