package com.application.mapa.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.application.mapa.R

@Composable
fun FullscreenProgress() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = { /* no-op, intended to intercept clicks */ }
            )
            .background(color = colorResource(id = R.color.progress_bg_color)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}