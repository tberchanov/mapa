package com.application.mapa.di

import android.content.Context
import com.application.mapa.feature.password.generator.GeneratedPasswordDataHolder
import com.application.mapa.feature.password.generator.manager.PasswordManager
import com.application.mapa.feature.password.generator.repository.PasswordGenerationSettingsRepository
import com.application.mapa.feature.password.generator.repository.PasswordGenerationSettingsRepositoryPrefs
import com.application.mapa.feature.password.generator.usecase.GeneratePasswordUseCase
import com.application.mapa.feature.password.generator.usecase.GetPasswordLengthRangeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class PasswordGeneratorModule {

    @Singleton
    @Provides
    fun providePasswordGenerationSettingsRepository(
        @ApplicationContext
        context: Context
    ): PasswordGenerationSettingsRepository = PasswordGenerationSettingsRepositoryPrefs(context)

    @Singleton
    @Provides
    fun providePasswordManager() = PasswordManager()

    @Singleton
    @Provides
    fun provideGeneratedPasswordDataHolder() = GeneratedPasswordDataHolder()

    @Provides
    fun provideGetPasswordLengthRangeUseCase() = GetPasswordLengthRangeUseCase()

    @Provides
    fun provideGeneratePasswordUseCase(
        passwordManager: PasswordManager
    ) = GeneratePasswordUseCase(passwordManager)
}