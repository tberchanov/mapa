package com.application.mapa.feature.settings.model

sealed class SettingsAction {
    class ChangeBooleanSetting(val id: SettingsId, val value: Boolean) : SettingsAction()
}