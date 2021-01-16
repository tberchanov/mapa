package com.application.mapa.feature.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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