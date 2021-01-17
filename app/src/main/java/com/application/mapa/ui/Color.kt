package com.application.mapa.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val purple200 = Color(0xFFBB86FC)
val purple500 = Color(0xFF6200EE)
val purple700 = Color(0xFF3700B3)
val teal200 = Color(0xFF03DAC5)
val darkTextColor = Color(0xFFE4E4E4)
val lightTextColor = Color(0xFF000000)
val white = Color(0xFFFFFFFF)
val darkDialogBackgroundColor = Color(0xFF222222)

@Composable
fun textColor() =
    if (isSystemInDarkTheme()) {
        darkTextColor
    } else {
        lightTextColor
    }

@Composable
fun dialogBackgroundColor() =
    if (isSystemInDarkTheme()) {
        darkDialogBackgroundColor
    } else {
        white
    }