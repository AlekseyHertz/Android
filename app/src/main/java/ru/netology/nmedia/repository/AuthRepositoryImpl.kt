import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.api.PostsApiService
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.dto.MediaUpload
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.error.NetworkError
import java.io.IOException
import java.util.concurrent.CancellationException
import javax.inject.Inject

interface AuthRepository {
    suspend fun signIn(
        login: String,
        password: String
    ): AppAuth.AuthState

    suspend fun registerNewUser(
        login: String,
        pass: String,
        name: String,
    ): AppAuth.AuthState

    suspend fun registerWithPhoto(
        login: String,
        pass: String,
        name: String,
        media: MediaUpload,
    ): AppAuth.AuthState
}

class AuthRepositoryImpl @Inject constructor(
    private val postsApiService: PostsApiService
) : AuthRepository {
    override suspend fun signIn(login: String, password: String): AppAuth.AuthState {
        try {
            val response = postsApiService.updateUser(login, password)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw NetworkError
        }
        return TODO("Provide the return value")
    }

    override suspend fun registerNewUser(login: String, pass: String, name: String): AppAuth.AuthState {
        try {

            val response = postsApiService.registerUser(login, pass, name)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: CancellationException) {
            throw CancellationException()
        } catch (e: Exception) {
            throw NetworkError
        }
        return TODO("Provide the return value")
    }

    override suspend fun registerWithPhoto(
        login: String,
        pass: String,
        name: String,
        media: MediaUpload
    ): AppAuth.AuthState {
        try {
            val file = MultipartBody.Part.createFormData(
                "file", media.file.name, media.file.asRequestBody()
            )
            val response = postsApiService.registerWithPhoto(
                login.toRequestBody("text/plain".toMediaType()),
                pass.toRequestBody("text/plain".toMediaType()),
                name.toRequestBody("text/plain".toMediaType()),
                file
            )
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            Log.e("error", e.message.toString())
            throw NetworkError
        }
        return TODO("Provide the return value")
    }
}