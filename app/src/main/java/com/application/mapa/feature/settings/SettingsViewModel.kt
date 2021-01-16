package com.application.mapa.feature.settings

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.mapa.feature.fingerprint.repository.CiphertextRepository
import com.application.mapa.feature.fingerprint.usecase.EncryptAndStoreDataUseCase
import com.application.mapa.feature.fingerprint.usecase.ShowBiometricPromptForEncryptionUseCase
import com.application.mapa.feature.settings.model.SettingsAction
import com.application.mapa.feature.settings.model.SettingsAction.*
import com.application.mapa.feature.settings.model.SettingsId
import com.application.mapa.feature.settings.model.SettingsItem
import com.application.mapa.feature.settings.model.SettingsState
import com.application.mapa.util.ActivityProvider

class SettingsViewModel @ViewModelInject constructor(
    getSettingsUseCase: GetSettingsUseCase,
    private val activityProvider: ActivityProvider,
    private val showBiometricPromptForEncryptionUseCase: ShowBiometricPromptForEncryptionUseCase,
    private val encryptAndStoreDataUseCase: EncryptAndStoreDataUseCase,
    private val ciphertextRepository: CiphertextRepository
) : ViewModel() {

    val state = MutableLiveData(SettingsState(emptyList(), false))

    init {
        getSettingsUseCase.execute().also {
            state.value = state.value?.copy(settingsList = it)
        }
    }

    fun postAction(action: SettingsAction): Unit = when (action) {
        is ChangeBooleanSetting -> processChangeBooleanSettings(action)
        is EnterPasswordDialogCancel -> {
            state.value = state.value?.copy(showEnterPasswordDialog = false)
        }
        is EnterPasswordDialogConfirm -> {
            state.value = state.value?.copy(showEnterPasswordDialog = false)
            processEnterPasswordDialogConfirm(action.password)
        }
    }

    private fun processEnterPasswordDialogConfirm(password: String) {
        activityProvider.getActivity()?.let { activity ->
            showBiometricPromptForEncryptionUseCase.execute(activity) {
                encryptAndStoreDataUseCase.execute(it, password)
                showFingerprintEnabled(true)
            }
        }
    }

    private fun processChangeBooleanSettings(action: ChangeBooleanSetting) {
        when (action.id) {
            SettingsId.FINGERPRINT -> processChangeFingerprintSetting(action.value)
        }
    }

    private fun processChangeFingerprintSetting(fingerprintEnable: Boolean) {
        if (fingerprintEnable) {
            state.value = state.value?.copy(showEnterPasswordDialog = true)
        } else {
            ciphertextRepository.clearCiphertext()
            showFingerprintEnabled(false)
        }
    }

    private fun showFingerprintEnabled(enabled: Boolean) {
        state.value = state.value?.run {
            copy(settingsList = changeFingerprintSetting(enabled, settingsList))
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