package ru.netology.nmedia.garbage

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