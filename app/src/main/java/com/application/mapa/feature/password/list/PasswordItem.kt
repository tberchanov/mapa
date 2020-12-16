package com.application.mapa.feature.password.list

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import com.application.mapa.data.model.Password

@Composable
fun PasswordItem(
    password: Password,
    onPasswordClick: (Password) -> Unit = {}
) {
    Button(onClick = { onPasswordClick(password) }) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            elevation = 8.dp,
        ) {
            Box {
                Text(
                    text = password.name,
                    modifier = Modifier.padding(8.dp),
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun PasswordItemPreview() {
    PasswordItem(password = Password(1, "name", "value"))
}