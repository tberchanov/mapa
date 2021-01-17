package com.application.mapa.feature.settings

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.application.mapa.feature.fingerprint.repository.CiphertextRepository
import com.application.mapa.feature.fingerprint.usecase.EncryptAndStoreDataUseCase
import com.application.mapa.feature.fingerprint.usecase.ShowBiometricPromptForEncryptionUseCase
import com.application.mapa.feature.settings.model.SettingsAction
import com.application.mapa.feature.settings.model.SettingsAction.*
import com.application.mapa.feature.settings.model.SettingsId
import com.application.mapa.feature.settings.model.SettingsItem
import com.application.mapa.feature.settings.model.SettingsState
import com.application.mapa.feature.settings.repository.SettingsRepository
import com.application.mapa.feature.settings.usecase.GetSettingsUseCase
import com.application.mapa.feature.settings.usecase.InitSettingsUseCase
import com.application.mapa.feature.settings.usecase.SetDarkThemeUseCase
import com.application.mapa.util.ActivityProvider

class SettingsViewModel @ViewModelInject constructor(
    getSettingsUseCase: GetSettingsUseCase,
    private val activityProvider: ActivityProvider,
    private val showBiometricPromptForEncryptionUseCase: ShowBiometricPromptForEncryptionUseCase,
    private val encryptAndStoreDataUseCase: EncryptAndStoreDataUseCase,
    private val ciphertextRepository: CiphertextRepository,
    private val setDarkThemeUseCase: SetDarkThemeUseCase,
    initSettingsUseCase: InitSettingsUseCase,
    settingsRepository: SettingsRepository
) : ViewModel() {

    val state = MutableLiveData(SettingsState(emptyList(), false))

    val darkThemeEnabled = settingsRepository.observeDarkThemeEnabled().asLiveData()

    init {
        initSettingsUseCase.execute()
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

    private fun processChangeBooleanSettings(
        action: ChangeBooleanSetting
    ) = when (action.id) {
        SettingsId.FINGERPRINT -> processChangeFingerprintSetting(action.value)
        SettingsId.DARK_THEME -> processChangeDarkThemeSetting(action.value)
    }

    private fun processChangeDarkThemeSetting(darkThemeEnabled: Boolean) {
        setDarkThemeUseCase.execute(darkThemeEnabled)
        showDarkThemeEnabled(darkThemeEnabled)
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

    private fun showDarkThemeEnabled(enabled: Boolean) {
        state.value = state.value?.run {
            copy(settingsList = changeDarkThemeSetting(enabled, settingsList))
        }
    }

    private fun changeFingerprintSetting(
        fingerprintEnabled: Boolean,
        settingsItems: List<SettingsItem>
    ): List<SettingsItem> {
        return changeBooleanSettings(fingerprintEnabled, SettingsId.FINGERPRINT, settingsItems)
    }

    private fun changeDarkThemeSetting(
        darkThemeEnable: Boolean,
        settingsItems: List<SettingsItem>
    ): List<SettingsItem> {
        return changeBooleanSettings(darkThemeEnable, SettingsId.DARK_THEME, settingsItems)
    }

    private fun changeBooleanSettings(
        value: Boolean,
        settingsId: SettingsId,
        settingsItems: List<SettingsItem>
    ): List<SettingsItem> {
        return settingsItems.map {
            if (it.id == settingsId) {
                it.copy(value = value)
            } else {
                it
            }
        }
    }
}