package com.application.mapa.di

import com.application.mapa.feature.encription.Decryptor
import com.application.mapa.feature.encription.Encryptor
import com.application.mapa.feature.encription.KeyGenerator
import com.application.mapa.feature.encription.StorableManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class EncryptionModule {

    @Singleton
    @Provides
    fun provideKeyGenerator() = KeyGenerator()

    @Singleton
    @Provides
    fun provideStorableManager() = StorableManager()

    @Singleton
    @Provides
    fun provideEncryptor(
        storableManager: StorableManager,
        keyGenerator: KeyGenerator
    ) = Encryptor(
        storableManager,
        keyGenerator
    )

    @Singleton
    @Provides
    fun provideDecryptor(
        storableManager: StorableManager,
        keyGenerator: KeyGenerator,
        encryptor: Encryptor
    ) = Decryptor(
        storableManager,
        keyGenerator,
        encryptor
    )
}