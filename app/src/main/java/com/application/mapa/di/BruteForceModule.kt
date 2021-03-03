package com.application.mapa.di

import android.content.Context
import com.application.mapa.feature.password.master.bruteforce.BruteForceRepository
import com.application.mapa.feature.password.master.bruteforce.BruteForceRepositoryPrefs
import com.application.mapa.feature.password.master.bruteforce.BruteForceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class BruteForceModule {

    @Singleton
    @Provides
    fun provideBruteForceRepository(
        @ApplicationContext context: Context
    ): BruteForceRepository = BruteForceRepositoryPrefs(context)

    @Provides
    fun provideCheckBruteForceUseCase(
        bruteForceRepository: BruteForceRepository
    ) = BruteForceManager(bruteForceRepository)
}