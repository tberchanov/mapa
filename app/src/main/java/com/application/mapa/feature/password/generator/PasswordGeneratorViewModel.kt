package com.application.mapa.feature.password.generator

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.mapa.feature.password.generator.model.PasswordGeneratorScreenAction
import com.application.mapa.feature.password.generator.model.PasswordGeneratorScreenAction.*
import com.application.mapa.feature.password.generator.model.PasswordGeneratorScreenState
import com.application.mapa.feature.password.generator.repository.PasswordGenerationSettingsRepository
import com.application.mapa.feature.password.generator.usecase.GeneratePasswordUseCase
import com.application.mapa.feature.password.generator.usecase.GeneratePasswordUseCase.Params
import com.application.mapa.feature.password.generator.usecase.GetPasswordLengthRangeUseCase

class PasswordGeneratorViewModel @ViewModelInject constructor(
    private val passwordGenerationSettingsRepository: PasswordGenerationSettingsRepository,
    private val getPasswordLengthRangeUseCase: GetPasswordLengthRangeUseCase,
    private val generatePasswordUseCase: GeneratePasswordUseCase,
    private val generatedPasswordDataHolder: GeneratedPasswordDataHolder
) : ViewModel() {

    val state = MutableLiveData(
        PasswordGeneratorScreenState(
            "",
            passwordGenerationSettingsRepository.getSettings(),
            passwordGenerationSettingsRepository.getPasswordLength(),
            false
        )
    )

    fun postAction(action: PasswordGeneratorScreenAction): Unit = when (action) {
        is ModifyGeneratedPassword -> processModifyGeneratedPasswordAction(action)
        is ModifyGenerationSetting -> processModifyGenerationSettingAction(action)
        is ModifyPasswordLength -> processModifyPasswordLengthAction(action)
        is GeneratePassword -> processGeneratePasswordAction()
        is ApplyPassword -> processApplyPasswordAction()
    }

    private fun processModifyGeneratedPasswordAction(action: ModifyGeneratedPassword) {
        state.value = state.value?.copy(generatedPassword = action.value)
    }

    private fun processModifyGenerationSettingAction(action: ModifyGenerationSetting) {
        val modifiedSettings = state.value
            ?.settings
            ?.indexOfFirst { it.id == action.setting.id }
            ?.let { settingIndex ->
                state.value
                    ?.settings
                    ?.toMutableList()
                    ?.apply {
                        set(settingIndex, action.setting)
                    }
            }
            ?.toList()

        if (modifiedSettings != null) {
            passwordGenerationSettingsRepository.saveSetting(action.setting)
            state.value = state.value?.copy(settings = modifiedSettings)
        }
    }

    private fun processModifyPasswordLengthAction(action: ModifyPasswordLength) {
        passwordGenerationSettingsRepository.savePasswordLength(action.length)
        state.value = state.value?.copy(length = action.length)
    }

    private fun processGeneratePasswordAction() {
        state.value?.apply {
            if (settings.any { it.enabled }) {
                val generatedPassword = generatePasswordUseCase.execute(Params(settings, length))
                state.value = state.value?.copy(
                    generatedPassword = generatedPassword,
                    showNoSettingEnabledError = false
                )
            } else {
                state.value = state.value?.copy(showNoSettingEnabledError = true)
            }
        }
    }

    private fun processApplyPasswordAction() {
        state.value?.apply {
            generatedPasswordDataHolder.putGeneratedPassword(generatedPassword)
        }
    }

    fun getPasswordLengthRange() = getPasswordLengthRangeUseCase.execute()
}