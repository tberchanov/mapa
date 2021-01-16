package com.application.mapa.feature.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.loadVectorResource
import androidx.compose.ui.res.stringResource
import com.application.mapa.R
import com.application.mapa.feature.settings.model.SettingsAction.EnterPasswordDialogCancel
import com.application.mapa.feature.settings.model.SettingsAction.EnterPasswordDialogConfirm
import com.application.mapa.feature.settings.ui.EnterPasswordDialog
import com.application.mapa.feature.settings.ui.SettingItem

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
                    IconButton(onClick = {
                        onBackClicked()
                    }) {
                        val imageRes = loadVectorResource(R.drawable.ic_arrow_back)
                        imageRes.resource.resource?.let {
                            Image(imageVector = it)
                        }
                    }
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