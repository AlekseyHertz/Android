package ru.netology.nmedia.viewmodel

/*class ViewModelFactory(
    private val repository: PostRepository,
    private val appAuth: AppAuth,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(PostViewModel::class.java) -> {
                PostViewModel(repository, appAuth) as T
            }

            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(appAuth) as T
            }

            else -> error("Unknown class : $modelClass")
        }
}*/