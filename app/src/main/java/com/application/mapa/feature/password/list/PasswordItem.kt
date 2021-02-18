package com.application.mapa.feature.password.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.mapa.feature.password.list.model.SelectablePassword

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PasswordItem(
    selectablePassword: SelectablePassword,
    selectionEnabled: Boolean,
    onPasswordClick: (SelectablePassword) -> Unit = {},
    onPasswordLongClick: (SelectablePassword) -> Unit = {},
    onPasswordChecked: (SelectablePassword) -> Unit = {},
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onPasswordClick(selectablePassword) },
                onLongClick = { onPasswordLongClick(selectablePassword) }
            ),
        elevation = 8.dp,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f),
                text = selectablePassword.password.name,
                fontSize = 20.sp
            )
            if (selectionEnabled) {
                Checkbox(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    checked = selectablePassword.selected,
                    onCheckedChange = {
                        onPasswordChecked(selectablePassword)
                    })
            }
        }
    }
}