package com.application.mapa.feature.settings.model

import androidx.annotation.DrawableRes

sealed class SettingsType {
    data class Switch(val value: Boolean) : SettingsType()
    data class Icon(@DrawableRes val iconRes: Int) : SettingsType()
}