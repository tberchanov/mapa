package com.application.mapa.ui.components

import androidx.compose.runtime.Composable
import com.application.mapa.R

@Composable
fun BackIconButton(
    onClick: () -> Unit
) {
    VectorIconButton(R.drawable.ic_arrow_back, onClick)
}