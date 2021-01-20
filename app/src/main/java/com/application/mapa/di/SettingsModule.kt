package com.application.mapa.di

import android.content.Context
import com.application.mapa.feature.fingerprint.repository.CiphertextRepository
import com.application.mapa.feature.settings.repository.SettingsRepository
import com.application.mapa.feature.settings.repository.SettingsRepositoryImpl
import com.application.mapa.feature.settings.usecase.GetSettingsUseCase
import com.application.mapa.feature.settings.usecase.InitSettingsUseCase
import com.application.mapa.feature.settings.usecase.SetDarkThemeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class SettingsModule {

    @ActivityRetainedScoped
    @Provides
    fun provideSettingsRepository(
        ciphertextRepository: CiphertextRepository,
        @ApplicationContext
        context: Context
    ): SettingsRepository = SettingsRepositoryImpl(ciphertextRepository, context)

    @Provides
    fun provideGetSettingsUseCase(
        @ApplicationContext
        context: Context,
        settingsRepository: SettingsRepository
    ) = GetSettingsUseCase(context, settingsRepository)

    @Provides
    fun provideInitSettingsUseCase(
        @ApplicationContext
        context: Context,
        settingsRepository: SettingsRepository
    ) = InitSettingsUseCase(context, settingsRepository)

    @Provides
    fun provideSetDarkThemeUseCase(
        settingsRepository: SettingsRepository
    ) = SetDarkThemeUseCase(settingsRepository)
}