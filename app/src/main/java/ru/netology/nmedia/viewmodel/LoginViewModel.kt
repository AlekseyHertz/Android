package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nmedia.api.PostsApiService
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.dto.Token
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.model.FeedModelState
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val appAuth: AppAuth,
    private val apiService: PostsApiService,
) : ViewModel() {
    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    private suspend fun login(login: String, password: String): Token {
        val response = try {
            apiService.updateUser(login, password)
        } catch (e: Exception) {
            throw Error("unknown exception")
        } catch (e: IOException) {
            throw Error("Network exception")
        }
        if (!response.isSuccessful) {
            throw ApiError(response.code(), response.message())
        }
        return response.body() ?: throw ApiError(
            status = response.code(),
            code = response.message()
        )
    }

    fun signLogin(login: String, password: String) {
        viewModelScope.launch {
            try {
                val result = login (login, password)
                appAuth.setAuth(result.id, result.token)
            } catch (e: ApiError) {
                _dataState.value = FeedModelState(error = true)
            } catch (e :Exception) {
                throw Error("unknown exception")
            }
        }
    }
}