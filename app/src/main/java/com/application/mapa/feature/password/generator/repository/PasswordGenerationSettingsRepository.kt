package com.application.mapa.feature.password.generator.repository

import com.application.mapa.feature.password.generator.model.PasswordGenerationSetting

interface PasswordGenerationSettingsRepository {

    fun getSettings(): List<PasswordGenerationSetting>

    fun saveSetting(setting: PasswordGenerationSetting)

    fun savePasswordLength(length: Int)

    fun getPasswordLength(): Int
}