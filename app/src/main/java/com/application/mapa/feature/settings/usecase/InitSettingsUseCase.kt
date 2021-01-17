package com.application.mapa.feature.settings.usecase

import androidx.appcompat.app.AppCompatDelegate
import com.application.mapa.feature.settings.repository.SettingsRepository

class InitSettingsUseCase(
    private val settingsRepository: SettingsRepository
) {

    fun execute() {
        if (!settingsRepository.isDarkThemeInitialized()) {
            settingsRepository.setDarkThemeEnabled(getDefaultNighModeEnabled())
        }
    }

    private fun getDefaultNighModeEnabled(): Boolean =
        AppCompatDelegate.getDefaultNightMode().let {
            AppCompatDelegate.MODE_NIGHT_YES == it
        }
}