package com.application.mapa.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.TextUnit

@Composable
fun ErrorText(text: String) {
    Text(text = text, color = MaterialTheme.colors.error)
}

@Composable
fun ErrorText(text: String, fontSize: TextUnit) {
    Text(text = text, color = MaterialTheme.colors.error, fontSize = fontSize)
}