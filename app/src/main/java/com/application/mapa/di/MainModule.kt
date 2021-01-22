package com.application.mapa.di

import com.application.mapa.feature.main.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
class MainModule {

    @Provides
    fun provideViewModelProvider() = ViewModelProvider()
}