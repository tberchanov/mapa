package com.application.mapa.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.loadVectorResource

@Composable
fun VectorIconButton(
    @DrawableRes resId: Int,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    IconButton(onClick = onClick, enabled = enabled) {
        val imageRes = loadVectorResource(resId)
        imageRes.resource.resource?.let {
            Image(imageVector = it)
        }
    }
}