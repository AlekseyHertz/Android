@file:OptIn(ExperimentalPagingApi::class)

package ru.netology.nmedia.repository // из PostRepositoryFileImpl

import android.annotation.SuppressLint
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.netology.nmedia.api.PostsApiService
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dao.PostRemoteKeyDao
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.AttachmentType
import ru.netology.nmedia.dto.FeedItem
import ru.netology.nmedia.dto.Media
import ru.netology.nmedia.dto.MediaUpload
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.error.NetworkError
import ru.netology.nmedia.error.UnknownError
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val dao: PostDao,
    private val apiService: PostsApiService,
    postRemoteKeyDao: PostRemoteKeyDao,
    appDb: AppDb,
) : PostRepository {
    @OptIn(ExperimentalPagingApi::class)
    override val data: Flow<PagingData<FeedItem>> = Pager(
        config = PagingConfig(pageSize = 5, enablePlaceholders = false),
        pagingSourceFactory = { dao.getPagingSource() },
        remoteMediator = PostRemoteMediator(
            apiService = apiService,
            postDao = dao,
            postRemoteKeyDao = postRemoteKeyDao,
            appDb = appDb,
        )
    ).flow.map {
        it.map(PostEntity::toDto)
    }

    override fun getNewerCount(id: Long): Flow<Int> =
        flow {
            while (true) {
                try {
                    delay(10_000)
                    val postsResponse = apiService.getNewer(id)
                    val body = postsResponse.body().orEmpty()
                    emit(body.size)
                    dao.insert(body.map(PostEntity::fromDto).map {
                        it.copy(hidden = true)
                    })
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    override suspend fun getById(id: Long) {
        val postResponse = apiService.getById(id)

    }

    @SuppressLint("SuspiciousIndentation")
    override suspend fun save(post: Post) {
        try {
            val response = apiService.save(post)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun saveWithAttachment(post: Post, media: MediaUpload, type: AttachmentType) {
        try {
            val upload = upload(media)
            val postWithAttach = post.copy(attachment = Attachment(upload.url, type))
            save(postWithAttach)
        } catch (e:ApiError) {
            throw e
        } catch (e: IOException) {
            throw NetworkError
        } catch (e:Exception) {
            throw UnknownError
        }
    }

    override suspend fun upload(upload: MediaUpload): Media {
        try {
            val media = MultipartBody.Part.createFormData(
                "file",
                upload.file.name,
                upload.file.asRequestBody()
            )
            val response = apiService.saveMedia(media)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun removeById(id: Long) {
        dao.removeById(id)
        try {
            val postsResponse = apiService.removeById(id)
            if (!postsResponse.isSuccessful) {
                throw RuntimeException(postsResponse.errorBody()?.string())
            }
        } catch (e: Exception) {
            throw Error("error")
        }
    }

    override suspend fun likeById(post: Post) {
        dao.likeById(post.id)
        try {
            val postResponse = if (!post.likedByMe) {
                apiService.likeById(post.id)
            } else {
                apiService.dislikeById(post.id)
            }
        } catch (e: Exception) {
            throw Error("error")
        }
    }

    override suspend fun updateFeed() {
        dao.updateFeed()
    }
}