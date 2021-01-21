package com.application.mapa.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.loadVectorResource

@Composable
fun VectorIconButton(
    @DrawableRes resId: Int,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        val imageRes = loadVectorResource(resId)
        imageRes.resource.resource?.let {
            Image(imageVector = it)
        }
    }
}