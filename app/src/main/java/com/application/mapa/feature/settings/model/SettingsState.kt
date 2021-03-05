package com.application.mapa.feature.settings.model

data class SettingsState(
    val settingsList: List<SettingsItem>,
    val showEnterPasswordDialog: Boolean,
    val showProgress: Boolean
)