package ru.netology.nmedia.repository.di

import android.content.Context
import androidx.room.Room
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.api.PostsApiService
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import java.util.concurrent.TimeUnit

// class DependencyContainer(
// private val context: Context
// ) {
//
// companion object {
// val glideDownloadUrl = "http://10.0.2.2:9999/"
//
// @Volatile
// private var instance: DependencyContainer? = null
//
// fun initApp(context: Context) {
// instance = DependencyContainer(context)
// }
//
// fun getInstance(): DependencyContainer {
// return instance!!
// }
// }
// val appAuth = AppAuth(context)
//
// private val client = OkHttpClient.Builder()
// .connectTimeout(60, TimeUnit.SECONDS)
// .addInterceptor { chain ->
// appAuth.authStateFlow.value?.token?.let { token ->
// val newRequest = chain.request().newBuilder()
// .addHeader("Authorization", token)
// .build()
// return@addInterceptor chain.proceed(newRequest)
// }
// chain.proceed(chain.request())
// }
//
// .addInterceptor(HttpLoggingInterceptor().apply {
// level = if (BuildConfig.DEBUG) {
// HttpLoggingInterceptor.Level.BODY
// } else {
// HttpLoggingInterceptor.Level.NONE
// }
// })
// .build()
//
// private val retrofit = Retrofit.Builder()
// .baseUrl(BuildConfig.BASE_URL)
// .addConverterFactory(GsonConverterFactory.create())
// .client(client)
// .build()
//
// private val appBd = Room.databaseBuilder(context, AppDb::class.java, "app.db")
// .fallbackToDestructiveMigration()
// .build()
//
// val apiService = retrofit.create<PostsApiService>()
// private val postDao = appBd.postDao()
//
//
// val repository: PostRepository = PostRepositoryImpl(
// postDao,
// apiService,
// )
// }