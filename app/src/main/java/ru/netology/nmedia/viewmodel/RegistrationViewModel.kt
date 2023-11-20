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
import ru.netology.nmedia.error.NetworkError
import ru.netology.nmedia.error.UnknownError
import ru.netology.nmedia.model.RegistrationState
import java.io.IOException
import java.net.HttpURLConnection
import javax.inject.Inject


@HiltViewModel
class RegistrationViewModel @Inject constructor (
    private val apiService : PostsApiService,
    private val appAuth: AppAuth

): ViewModel() {
    private val _dataState = MutableLiveData<RegistrationState>()
    val dataState: LiveData<RegistrationState>
        get() = _dataState

    fun tryRegistration(login: String, password: String, username: String) {
        viewModelScope.launch {
            try {
                val result = registration(login, password, username)
                appAuth.setAuth(result.id, result.token)

            } catch (e: ApiError) {
                _dataState.value = when (e.status) {
                    HttpURLConnection.HTTP_CONFLICT -> {
                        RegistrationState(userAlreadyExists = true)
                    }

                    HttpURLConnection.HTTP_BAD_REQUEST -> {
                        RegistrationState(error = true)
                    }

                    else -> {
                        RegistrationState(error = true)
                    }
                }
            } catch (e: Exception) {
                _dataState.value = RegistrationState(error = true)
            }
        }
    }

    private suspend fun registration(login: String, password: String, username: String): Token {

        val response = try {
            apiService.registerUser(login, password, username)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
        if (!response.isSuccessful) {
            throw ApiError(status = response.code(), code = response.message())
        }
        return response.body() ?: throw ApiError(
            status = response.code(),
            code = response.message()
        )
    }
}