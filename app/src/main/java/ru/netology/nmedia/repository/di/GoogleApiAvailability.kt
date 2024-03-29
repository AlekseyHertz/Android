package ru.netology.nmedia.repository.di

import com.google.android.gms.common.GoogleApiAvailability
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object GoogleApiModule {

    @Singleton
    @Provides
    fun bindsGoogleApi() = GoogleApiAvailability.getInstance()
}