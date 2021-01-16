package com.application.mapa.di

import android.content.Context
import com.application.mapa.data.database.AppDatabase
import com.application.mapa.data.database.dao.PasswordDao
import com.application.mapa.feature.encryption.database.Decryptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabaseFactory(
        @ApplicationContext context: Context,
        decryptor: Decryptor
    ) = DatabaseFactory(context, decryptor)

    @Singleton
    @Provides
    fun provideDatabase(
        databaseFactory: DatabaseFactory
    ): AppDatabase = databaseFactory.getDatabase()

    @Singleton
    @Provides
    fun providePasswordDao(
        appDatabase: AppDatabase
    ): PasswordDao = appDatabase.passwordDao()
}