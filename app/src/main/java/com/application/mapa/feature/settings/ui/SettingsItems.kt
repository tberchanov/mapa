package com.application.mapa.feature.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.mapa.feature.settings.model.SettingsAction
import com.application.mapa.feature.settings.model.SettingsItem
import com.application.mapa.feature.settings.model.SettingsType

@Composable
fun SettingItem(
    item: SettingsItem,
    onPostAction: (SettingsAction) -> Unit
) {
    when (item.type) {
        is SettingsType.Switch ->
            SwitchSettingItem(item.type, item) {
                onPostAction(SettingsAction.BooleanSettingChanged(item.id, !item.type.value))
            }
        is SettingsType.Icon -> {
            IconSettingItem(item.type, item) {
                onPostAction(SettingsAction.SettingClicked(item.id))
            }
        }
    }
}

@Composable
fun SwitchSettingItem(
    type: SettingsType.Switch,
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
                checked = type.value,
                onCheckedChange = {
                    onSettingClicked(item)
                }
            )
        }
    }
}

@Composable
fun IconSettingItem(
    type: SettingsType.Icon,
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
            Icon(
                modifier = Modifier.padding(horizontal = 8.dp),
                painter = painterResource(id = type.iconRes), contentDescription = null
            )
        }
    }
}