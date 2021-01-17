package com.application.mapa.feature.settings.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun isFingerprintEnabled(): Boolean

    fun isDarkThemeInitialized(): Boolean

    fun isDarkThemeEnabled(): Boolean

    fun setDarkThemeEnabled(enabled: Boolean)

    fun observeDarkThemeEnabled(): Flow<Boolean>
}