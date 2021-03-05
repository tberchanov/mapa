package com.application.mapa.feature.settings

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.application.mapa.feature.fingerprint.repository.CiphertextRepository
import com.application.mapa.feature.fingerprint.usecase.EncryptAndStoreDataUseCase
import com.application.mapa.feature.fingerprint.usecase.ShowBiometricPromptForEncryptionUseCase
import com.application.mapa.feature.settings.model.*
import com.application.mapa.feature.settings.model.SettingsAction.*
import com.application.mapa.feature.settings.repository.SettingsRepository
import com.application.mapa.feature.settings.usecase.ExportDataUseCase
import com.application.mapa.feature.settings.usecase.GetSettingsUseCase
import com.application.mapa.feature.settings.usecase.InitSettingsUseCase
import com.application.mapa.feature.settings.usecase.SetDarkThemeUseCase
import com.application.mapa.util.ActivityProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface SettingsViewModel {
    val state: LiveData<SettingsState>
    val darkThemeEnabled: LiveData<Boolean>
    fun postAction(action: SettingsAction)
}

class SettingsViewModelImpl @ViewModelInject constructor(
    getSettingsUseCase: GetSettingsUseCase,
    private val activityProvider: ActivityProvider,
    private val showBiometricPromptForEncryptionUseCase: ShowBiometricPromptForEncryptionUseCase,
    private val encryptAndStoreDataUseCase: EncryptAndStoreDataUseCase,
    private val ciphertextRepository: CiphertextRepository,
    private val setDarkThemeUseCase: SetDarkThemeUseCase,
    initSettingsUseCase: InitSettingsUseCase,
    settingsRepository: SettingsRepository,
    private val exportDataUseCase: ExportDataUseCase
) : ViewModel(), SettingsViewModel {

    override val state = MutableLiveData(SettingsState(emptyList(), false, showProgress = false))

    override val darkThemeEnabled = settingsRepository.observeDarkThemeEnabled().asLiveData()

    init {
        initSettingsUseCase.execute()
        getSettingsUseCase.execute().also {
            state.value = state.value?.copy(settingsList = it)
        }
    }

    override fun postAction(action: SettingsAction): Unit = when (action) {
        is BooleanSettingChanged -> processChangeBooleanSettings(action)
        is EnterPasswordDialogCancel -> {
            state.value = state.value?.copy(showEnterPasswordDialog = false)
        }
        is EnterPasswordDialogConfirm -> {
            state.value = state.value?.copy(showEnterPasswordDialog = false)
            processEnterPasswordDialogConfirm(action.password)
        }
        is SettingClicked -> processSettingsClicked(action)
    }

    private fun processSettingsClicked(action: SettingClicked) {
        when (action.id) {
            SettingsId.IMPORT_DATA -> {
                // TODO implement import data
            }
            SettingsId.EXPORT_DATA -> exportData()
            else -> Log.d("SettingsViewModel", "${action.id} click is not processed!")
        }
    }

    private fun exportData() {
        viewModelScope.launch(Dispatchers.Main) {
            state.value = state.value?.copy(showProgress = true)
            withContext(Dispatchers.IO) {
                exportDataUseCase.execute()
            }
            state.value = state.value?.copy(showProgress = false)
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

    private fun processChangeBooleanSettings(action: BooleanSettingChanged) {
        when (action.id) {
            SettingsId.FINGERPRINT -> processChangeFingerprintSetting(action.value)
            SettingsId.DARK_THEME -> processChangeDarkThemeSetting(action.value)
            else -> Log.d("SettingsViewModel", "${action.id} is not a boolean setting!")
        }
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
        return changeSettingType(
            SettingsType.Switch(fingerprintEnabled),
            SettingsId.FINGERPRINT,
            settingsItems
        )
    }

    private fun changeDarkThemeSetting(
        darkThemeEnable: Boolean,
        settingsItems: List<SettingsItem>
    ): List<SettingsItem> {
        return changeSettingType(
            SettingsType.Switch(darkThemeEnable),
            SettingsId.DARK_THEME,
            settingsItems
        )
    }

    private fun changeSettingType(
        type: SettingsType,
        settingsId: SettingsId,
        settingsItems: List<SettingsItem>
    ): List<SettingsItem> {
        return settingsItems.map {
            if (it.id == settingsId) {
                it.copy(type = type)
            } else {
                it
            }
        }
    }
}