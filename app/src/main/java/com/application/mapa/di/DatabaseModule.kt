package com.application.mapa.di

import android.content.Context
import com.application.mapa.data.database.AppDatabase
import com.application.mapa.data.database.dao.PasswordDao
import com.application.mapa.feature.encription.Decryptor
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
    fun provideDatabase(
        @ApplicationContext context: Context,
        decryptor: Decryptor
    ): AppDatabase = AppDatabase.getInstance(
        "passcode" /*TODO should be receive from user*/,
        context,
        decryptor
    )

    @Singleton
    @Provides
    fun providePasswordDao(
        appDatabase: AppDatabase
    ): PasswordDao = appDatabase.passwordDao()
}