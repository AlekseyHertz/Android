package ru.netology.nmedia.repository.di

import com.google.firebase.messaging.FirebaseMessaging
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
interface FirebaseMessagingModule {

    @Singleton
    @Binds
    fun bindsFirebaseMessaging(
        fbmApp: FirebaseMessaging
    ): FirebaseMessaging
}
/*fun bindsFirebaseMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()
}*/