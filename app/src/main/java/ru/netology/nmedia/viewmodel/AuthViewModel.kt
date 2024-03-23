package ru.netology.nmedia.vi

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import ru.netology.nmedia.auth.AppAuth
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val appAuth: AppAuth,
) : ViewModel() {
    val data: LiveData<AppAuth.AuthState> = appAuth
        .authStateFlow
        //val state = AppAuth.getInstance().authFlow
        .asLiveData(Dispatchers.Default)
    val authorized: Boolean
        get() = appAuth.authStateFlow.value.id != 0L
}