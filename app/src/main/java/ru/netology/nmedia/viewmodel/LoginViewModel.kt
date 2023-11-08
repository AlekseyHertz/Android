package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.nmedia.api.PostApi
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.dto.Token
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.model.FeedModelState
import java.io.IOException

class LoginViewModel : ViewModel() {
    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    suspend fun login(username: String, password: String): Token {
        try {
            val response = PostApi.retrofitService.updateUser(username, password)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val token = response.body() ?: throw ApiError(response.code(), response.message())
            return token
        } catch (e: Exception) {
            throw Error("unknown exception")
        } catch (e: IOException) {
            throw Error("Network exception")
        }
    }

    fun signLogin(username: String, password: String) {
        viewModelScope.launch {
            try {
                val result = login(username, password)
                AppAuth.getInstance().setAuth(result)
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }
}