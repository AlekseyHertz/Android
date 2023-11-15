package ru.netology.nmedia.repository.di

import com.google.android.gms.common.GoogleApiAvailability
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface GoogleApiModule {

    @Singleton
    @Binds
    fun bindsGoogleApi(
        googleApi: GoogleApiAvailability
    ): GoogleApiAvailability
}
/* fun bindsGoogleApi(): GoogleApiAvailability = GoogleApiAvailability.getInstance()
}*/