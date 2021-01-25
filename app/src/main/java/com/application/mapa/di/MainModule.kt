package com.application.mapa.di

import android.content.Context
import com.application.mapa.feature.check.root.CheckRootUseCase
import com.application.mapa.feature.main.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ApplicationComponent::class)
class MainModule {

    @Provides
    fun provideViewModelProvider() = ViewModelProvider()

    @Provides
    fun provideCheckRootUseCase(
        @ApplicationContext
        context: Context
    ) = CheckRootUseCase(context)
}