package com.application.mapa.di

import android.content.Context
import com.application.mapa.feature.encryption.database.Decryptor
import com.application.mapa.feature.encryption.database.Encryptor
import com.application.mapa.feature.encryption.database.KeyGenerator
import com.application.mapa.feature.encryption.database.storable.StorableManager
import com.application.mapa.feature.encryption.database.storable.StorableManagerCryptoFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
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