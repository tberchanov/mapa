package com.application.mapa.feature.settings

import android.content.Context
import com.application.mapa.R
import com.application.mapa.feature.settings.model.SettingsId
import com.application.mapa.feature.settings.model.SettingsItem
import com.application.mapa.feature.settings.model.SettingsType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    @ApplicationContext
    private val context: Context
) {

    fun execute(): List<SettingsItem> {
        return listOf(
            SettingsItem(
                context.getString(R.string.fingerprint_setting),
                SettingsId.FINGERPRINT,
                SettingsType.SWITCH,
                false // CiphertextRepository.hasCiphertextSaved
            )
        )
    }
}