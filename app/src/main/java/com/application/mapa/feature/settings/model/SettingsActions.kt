package com.application.mapa.feature.settings.model

sealed class SettingsAction {
    data class BooleanSettingChanged(val id: SettingsId, val value: Boolean) : SettingsAction()
    data class SettingClicked(val id: SettingsId) : SettingsAction()
    object EnterPasswordDialogCancel : SettingsAction()
    data class EnterPasswordDialogConfirm(val password: String) : SettingsAction()
}