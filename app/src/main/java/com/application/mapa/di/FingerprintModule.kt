package com.application.mapa.di

import android.content.Context
import com.application.mapa.feature.fingerprint.CryptographyManager
import com.application.mapa.feature.fingerprint.repository.CiphertextRepository
import com.application.mapa.feature.fingerprint.repository.CiphertextRepositoryImpl
import com.application.mapa.feature.fingerprint.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ApplicationComponent::class)
class FingerprintModule {

    @Provides
    fun provideCiphertextRepository(
        @ApplicationContext
        context: Context
    ): CiphertextRepository = CiphertextRepositoryImpl(context)

    @Provides
    fun provideCryptographyManager(): CryptographyManager = CryptographyManager()

    @Provides
    fun provideCreateBiometricPromptUseCase(
        @ApplicationContext
        context: Context
    ) = CreateBiometricPromptUseCase(context)

    @Provides
    fun provideCreatePromptInfoUseCase() = CreatePromptInfoUseCase()

    @Provides
    fun provideDecryptDataFromStorageUseCase(
        cryptographyManager: CryptographyManager,
        ciphertextRepository: CiphertextRepository
    ) = DecryptDataFromStorageUseCase(cryptographyManager, ciphertextRepository)

    @Provides
    fun provideEncryptAndStoreDataUseCase(
        cryptographyManager: CryptographyManager,
        ciphertextRepository: CiphertextRepository
    ) = EncryptAndStoreDataUseCase(cryptographyManager, ciphertextRepository)

    @Provides
    fun provideShowBiometricPromptForDecryptionUseCase(
        @ApplicationContext
        context: Context,
        cryptographyManager: CryptographyManager,
        createBiometricPromptUseCase: CreateBiometricPromptUseCase,
        createPromptInfoUseCase: CreatePromptInfoUseCase,
        ciphertextRepository: CiphertextRepository
    ) = ShowBiometricPromptForDecryptionUseCase(
        context,
        cryptographyManager,
        createBiometricPromptUseCase,
        createPromptInfoUseCase,
        ciphertextRepository
    )

    @Provides
    fun provideShowBiometricPromptForEncryptionUseCase(
        @ApplicationContext
        context: Context,
        cryptographyManager: CryptographyManager,
        createBiometricPromptUseCase: CreateBiometricPromptUseCase,
        createPromptInfoUseCase: CreatePromptInfoUseCase,
    ) = ShowBiometricPromptForEncryptionUseCase(
        context,
        cryptographyManager,
        createBiometricPromptUseCase,
        createPromptInfoUseCase,
    )
}