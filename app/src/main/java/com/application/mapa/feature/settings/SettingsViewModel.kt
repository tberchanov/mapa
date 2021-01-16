package com.application.mapa.feature.settings

import androidx.appcompat.app.AppCompatActivity
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.mapa.feature.fingerprint.usecase.EncryptAndStoreDataUseCase
import com.application.mapa.feature.fingerprint.usecase.ShowBiometricPromptForEncryptionUseCase
import com.application.mapa.feature.settings.model.SettingsAction
import com.application.mapa.feature.settings.model.SettingsAction.ChangeBooleanSetting
import com.application.mapa.feature.settings.model.SettingsId
import com.application.mapa.feature.settings.model.SettingsItem
import com.application.mapa.feature.settings.model.SettingsState

class SettingsViewModel @ViewModelInject constructor(
    getSettingsUseCase: GetSettingsUseCase,
    private val showBiometricPromptForEncryptionUseCase: ShowBiometricPromptForEncryptionUseCase,
    private val encryptAndStoreDataUseCase: EncryptAndStoreDataUseCase
) : ViewModel() {

    val state = MutableLiveData(SettingsState(emptyList()))

    // TODO use ActivityProvider instead
    lateinit var activity: AppCompatActivity

    init {
        getSettingsUseCase.execute().also {
            state.value = state.value?.copy(settingsList = it)
        }
    }

    fun postAction(action: SettingsAction) {
        when (action) {
            is ChangeBooleanSetting -> processChangeBooleanSettings(action)
        }
    }

    private fun processChangeBooleanSettings(action: ChangeBooleanSetting) {
        when (action.id) {
            SettingsId.FINGERPRINT -> processChangeFingerprintSetting(action.value)
        }
    }

    private fun processChangeFingerprintSetting(value: Boolean) {
        showBiometricPromptForEncryptionUseCase.execute(activity) {
            // TODO need to get passcode from the user
            encryptAndStoreDataUseCase.execute(it, "passcode")
        }

        state.value = state.value?.run {
            copy(settingsList = changeFingerprintSetting(value, settingsList))
        }
    }

    private fun changeFingerprintSetting(
        fingerprintValue: Boolean,
        settingsItems: List<SettingsItem>
    ): List<SettingsItem> {
        return settingsItems.map {
            if (it.id == SettingsId.FINGERPRINT) {
                it.copy(value = fingerprintValue)
            } else {
                it
            }
        }
    }
}