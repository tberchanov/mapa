package com.application.mapa.di

import android.content.Context
import com.application.mapa.feature.encription.Decryptor
import com.application.mapa.feature.encription.Encryptor
import com.application.mapa.feature.encription.KeyGenerator
import com.application.mapa.feature.encription.storable.StorableManager
import com.application.mapa.feature.encription.storable.StorableManagerCryptoFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class EncryptionModule {

    @Singleton
    @Provides
    fun provideKeyGenerator() = KeyGenerator()

    @Singleton
    @Provides
    fun provideStorableManager(
        @ApplicationContext
        context: Context
    ): StorableManager = StorableManagerCryptoFile(context)

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