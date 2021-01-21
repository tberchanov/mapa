package com.application.mapa.feature.settings.repository

import android.content.Context
import android.content.SharedPreferences
import com.application.mapa.feature.fingerprint.repository.CiphertextRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsRepositoryPrefs(
    private val ciphertextRepository: CiphertextRepository,
    context: Context,
) : SettingsRepository {

    private val settingsPrefs = context.getSharedPreferences(SETTINGS_PREFS, Context.MODE_PRIVATE)

    private val darkThemeEnabledFlow = MutableStateFlow(isDarkThemeEnabled())
    private val darkThemeChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == DARK_THEME_SETTING_KEY) {
                darkThemeEnabledFlow.value = isDarkThemeEnabled()
            }
        }

    override fun isFingerprintEnabled(): Boolean {
        return ciphertextRepository.hasCiphertextSaved()
    }

    override fun isDarkThemeInitialized(): Boolean {
        return settingsPrefs.contains(DARK_THEME_SETTING_KEY)
    }

    override fun isDarkThemeEnabled(): Boolean {
        return settingsPrefs.getBoolean(DARK_THEME_SETTING_KEY, false)
    }

    override fun setDarkThemeEnabled(enabled: Boolean) {
        settingsPrefs.edit()
            .putBoolean(DARK_THEME_SETTING_KEY, enabled)
            .apply()
    }

    override fun observeDarkThemeEnabled(): Flow<Boolean> {
        settingsPrefs.registerOnSharedPreferenceChangeListener(darkThemeChangeListener)
        return darkThemeEnabledFlow
    }

    companion object {
        private const val SETTINGS_PREFS = "settings"
        private const val DARK_THEME_SETTING_KEY = "DARK_THEME_SETTING_KEY"
    }
}