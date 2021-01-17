package com.application.mapa.feature.settings.usecase

import com.application.mapa.feature.settings.repository.SettingsRepository

class SetDarkThemeUseCase(
    private val settingsRepository: SettingsRepository
) {

    fun execute(darkThemeEnabled: Boolean) {
        settingsRepository.setDarkThemeEnabled(darkThemeEnabled)
    }
}