package com.application.mapa.feature.settings.usecase

import android.content.Context
import com.application.mapa.R
import com.application.mapa.feature.settings.model.SettingsId
import com.application.mapa.feature.settings.model.SettingsItem
import com.application.mapa.feature.settings.model.SettingsType
import com.application.mapa.feature.settings.repository.SettingsRepository

class GetSettingsUseCase(
    private val context: Context,
    private val settingsRepository: SettingsRepository
) {

    fun execute(): List<SettingsItem> {
        return listOf(
            SettingsItem(
                context.getString(R.string.fingerprint_setting),
                SettingsId.FINGERPRINT,
                SettingsType.Switch(settingsRepository.isFingerprintEnabled()),
            ),
            SettingsItem(
                context.getString(R.string.dark_theme_setting),
                SettingsId.DARK_THEME,
                SettingsType.Switch(settingsRepository.isDarkThemeEnabled())
            ),
            SettingsItem(
                context.getString(R.string.export_data_setting),
                SettingsId.EXPORT_DATA,
                SettingsType.Icon(R.drawable.ic_export)
            ),
            SettingsItem(
                context.getString(R.string.import_data_setting),
                SettingsId.IMPORT_DATA,
                SettingsType.Icon(R.drawable.ic_import)
            )
        )
    }
}