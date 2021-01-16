package com.application.mapa.feature.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.loadVectorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.mapa.R
import com.application.mapa.feature.settings.model.SettingsAction
import com.application.mapa.feature.settings.model.SettingsItem
import com.application.mapa.feature.settings.model.SettingsType

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBackClicked: () -> Unit,
) {
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
            val state by viewModel.state.observeAsState()
            LazyColumn {
                state?.settingsList?.let { settingsList ->
                    items(settingsList) {
                        SettingItem(it, viewModel::postAction)
                    }
                }
            }
        }
    )
}

@Composable
fun SettingItem(
    item: SettingsItem,
    onPostAction: (SettingsAction) -> Unit
) {
    when (item.type) {
        SettingsType.SWITCH ->
            SwitchSettingItem(
                item = item,
                onSettingClicked = {
                    onPostAction(SettingsAction.ChangeBooleanSetting(item.id, !item.value))
                }
            )
    }
}

@Composable
fun SwitchSettingItem(
    item: SettingsItem,
    onSettingClicked: (SettingsItem) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(
                onClick = { onSettingClicked(item) },
            ),
        elevation = 8.dp,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f),
                text = item.title,
                fontSize = 20.sp
            )
            Switch(
                modifier = Modifier.padding(horizontal = 8.dp),
                checked = item.value,
                onCheckedChange = {
                    onSettingClicked(item)
                }
            )
        }
    }
}