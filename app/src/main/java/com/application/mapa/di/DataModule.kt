package com.application.mapa.di

import com.application.mapa.data.database.dao.PasswordDao
import com.application.mapa.data.repository.PasswordRepository
import com.application.mapa.data.repository.PasswordRepositoryDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class DataModule {

    @ActivityRetainedScoped
    @Provides
    fun bindPasswordRepository(
        passwordDao: PasswordDao
    ): PasswordRepository = PasswordRepositoryDB(passwordDao)
}