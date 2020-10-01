package com.application.mapa.di

import com.application.mapa.data.repository.PasswordRepository
import com.application.mapa.data.repository.PasswordRepositoryDB
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindPasswordRepository(
        passwordRepositoryDB: PasswordRepositoryDB
    ): PasswordRepository
}