package com.application.mapa.di

import com.application.mapa.util.ActivityProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class ActivityProviderModule {

    @ActivityRetainedScoped
    @Provides
    fun provideActivityProvider() = ActivityProvider()
}