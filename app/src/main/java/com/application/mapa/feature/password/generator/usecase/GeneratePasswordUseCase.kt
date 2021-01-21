package com.application.mapa.feature.password.generator.usecase

import com.application.mapa.feature.password.generator.manager.PasswordManager
import com.application.mapa.feature.password.generator.model.*

class GeneratePasswordUseCase(
    private val passwordManager: PasswordManager
) {

    fun execute(params: Params): String {
        return passwordManager.generatePassword(
            isWithLetters = getGenerationSettingValue(params, LOW_CASE_SYMBOLS),
            isWithUppercase = getGenerationSettingValue(params, UPPER_CASE_SYMBOLS),
            isWithNumbers = getGenerationSettingValue(params, NUMBERS),
            isWithSpecial = getGenerationSettingValue(params, SPECIAL_SYMBOLS),
            length = params.passwordLength
        )
    }

    private fun getGenerationSettingValue(params: Params, id: Int): Boolean {
        return params.generationSettings.find { it.id == id }?.enabled ?: false
    }

    data class Params(
        val generationSettings: List<PasswordGenerationSetting>,
        val passwordLength: Int
    )
}