package com.application.mapa.feature.password.generator.repository

import android.content.Context
import com.application.mapa.R
import com.application.mapa.feature.password.generator.model.*

class PasswordGenerationSettingsRepositoryPrefs(
    context: Context
) : PasswordGenerationSettingsRepository {

    private val settingsPrefs = context.getSharedPreferences(SETTINGS_PREFS, Context.MODE_PRIVATE)

    override fun getSettings(): List<PasswordGenerationSetting> {
        return listOf(
            PasswordGenerationSetting(
                R.string.upper_case_symbols,
                UPPER_CASE_SYMBOLS,
                getSettingEnabled(UPPER_CASE_SYMBOLS)
            ),
            PasswordGenerationSetting(
                R.string.low_case_symbols,
                LOW_CASE_SYMBOLS,
                getSettingEnabled(LOW_CASE_SYMBOLS)
            ),
            PasswordGenerationSetting(
                R.string.numbers,
                NUMBERS,
                getSettingEnabled(NUMBERS)
            ),
            PasswordGenerationSetting(
                R.string.special_symbols,
                SPECIAL_SYMBOLS,
                getSettingEnabled(SPECIAL_SYMBOLS)
            ),
        )
    }

    private fun getSettingEnabled(settingId: Int) =
        settingsPrefs.getBoolean(settingId.toString(), false)

    override fun saveSetting(setting: PasswordGenerationSetting) {
        settingsPrefs.edit()
            .putBoolean(setting.id.toString(), setting.enabled)
            .apply()
    }

    override fun savePasswordLength(length: Int) {
        settingsPrefs.edit()
            .putInt(KEY_PASSWORD_LENGTH, length)
            .apply()
    }

    override fun getPasswordLength(): Int {
        return settingsPrefs.getInt(KEY_PASSWORD_LENGTH, 1)
    }

    companion object {
        private const val SETTINGS_PREFS = "settings"
        private const val KEY_PASSWORD_LENGTH = "PASSWORD_LENGTH"
    }
}