package com.application.mapa.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.application.mapa.R

@Composable
fun ErrorMessage(
    message: String,
    description: String
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            text = { Text(description) },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp, end = 10.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { showDialog = false }
                    ) {
                        Text(text = stringResource(R.string.ok))
                    }
                }
            }
        )
    }

    Card(
        modifier = Modifier.clickable(onClick = { showDialog = true }),
        shape = RoundedCornerShape(14.dp),
        backgroundColor = MaterialTheme.colors.error
    ) {
        Text(modifier = Modifier.padding(8.dp), text = message)
    }
}