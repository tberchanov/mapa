package com.application.mapa.feature.settings.model

data class SettingsItem(
    val title: String,
    val id: SettingsId,
    val type: SettingsType,
    val value: Boolean
)