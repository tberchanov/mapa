package com.application.mapa.feature.settings.usecase

import android.content.Context
import android.content.res.Configuration
import com.application.mapa.feature.settings.repository.SettingsRepository

class InitSettingsUseCase(
    private val context: Context,
    private val settingsRepository: SettingsRepository
) {

    fun execute() {
        if (!settingsRepository.isDarkThemeInitialized()) {
            settingsRepository.setDarkThemeEnabled(getDefaultNighModeEnabled())
        }
    }

    private fun getDefaultNighModeEnabled(): Boolean =
        (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK).let {
            Configuration.UI_MODE_NIGHT_YES == it
        }
}