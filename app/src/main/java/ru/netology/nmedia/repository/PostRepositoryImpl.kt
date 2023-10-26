package ru.netology.nmedia.repository // из PostRepositoryFileImpl

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.netology.nmedia.api.PostApi
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.AttachmentType
import ru.netology.nmedia.dto.Media
import ru.netology.nmedia.dto.MediaUpload
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.error.NetworkError
import ru.netology.nmedia.error.UnknownError
import java.io.IOException

class PostRepositoryImpl(
    private val dao: PostDao
) : PostRepository {
    override val data = dao.getAll()
        .map { it.map(PostEntity::toDto) }
        .flowOn(Dispatchers.Default)

    override fun getNewerCount(id: Long): Flow<Int> =
        flow {
            while (true) {
                try {
                    delay(10_000)
                    val postsResponse = PostApi.retrofitService.getNewer(id)
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

    override suspend fun getAll() {
        try {
            val postsResponse = PostApi.retrofitService.getAll()
            if (!postsResponse.isSuccessful) {
                throw RuntimeException(postsResponse.errorBody()?.string())
            }
            val posts = postsResponse.body() ?: throw java.lang.RuntimeException("body is null")
            dao.insert(posts.map(PostEntity::fromDto))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun getById(id: Long) {
        val postResponse = PostApi.retrofitService.getById(id)

    }

    override suspend fun save(post: Post) {
        try {
            val response = PostApi.retrofitService.save(post)
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

    override suspend fun saveWithAttachment(post: Post, upload: MediaUpload) {
        try {
            val media = upload(upload)
            val response = PostApi.retrofitService.save(
                post.copy(
                    attachment = Attachment(
                        url = media.id,
                        type = AttachmentType.IMAGE
                    )
                )
            )
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(PostEntity.fromDto(body))


//            val response = post.copy(attachment = Attachment(media.id, AttachmentType.IMAGE))
//            save(response)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    private suspend fun upload(upload: MediaUpload): Media {
        try {
            val part = MultipartBody.Part.createFormData(
                "name",
                upload.file.name,
                upload.file.asRequestBody()
            )

            val response = PostApi.retrofitService.saveMedia(part)
            if (!response.isSuccessful) {
                throw RuntimeException(response.errorBody()?.string())
            }
            return requireNotNull(response.body())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun removeById(id: Long) {
        dao.removeById(id)
        try {
            val postsResponse = PostApi.retrofitService.removeById(id)
            if (!postsResponse.isSuccessful) {
                throw RuntimeException(postsResponse.errorBody()?.string())
            }
        } catch (e: Exception) {
            throw Error("error")
        }
    }

    /*override fun viewById(id: Long) {}= dao.viewById(id) //{
    posts = posts.map {
        if (it.id != id) it else it.copy(
            viewsCount = it.viewsCount + 1
        )
    }
    data.value = posts

     */
    /*= dao.removeById(id) {
        dao.removeById(id)
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun removeByIdAsync(id: Long, callback: PostRepository.Callback<Unit>) {
        PostApi.service.deletePost(id)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    val body = response.body() ?: throw RuntimeException("body is null")
                    callback.onSuccess(Unit)
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(Exception(t))
                }
            })
    }*/


    /*override fun getAllAsync(callback: PostRepository.Callback<List<Post>>) {
        return PostApi.service.getAll()
            .enqueue(object : Callback<List<Post>> {
                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.errorBody()?.string()))
                        return
                    }
                    val body = response.body() ?: run {
                        callback.onError(RuntimeException("response is null"))
                        return
                    }
                    callback.onSuccess(body)
                }

                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    callback.onError(Exception(t))
                }
            })
    }


    override fun getAllAsync(callback: PostRepository.Callback<List<Post>>) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    try {
                        val body = response.body?.string() ?: throw RuntimeException("body is null")
                        callback.onSuccess(gson.fromJson(body, typeToken.type))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }
     */

    /*init {
    }
        posts = dao.getAll()
        data.value = posts
    }*/


    /*override fun getAll(): LiveData<List<Post>> =
        dao.getAll().map { list -> list.map { it.toDto() } }

    override fun likeByIdAsync(id: Long, callback: PostRepository.Callback<Post>) {
        PostApi.service.likePost(id)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    val body =
                        response.body() ?: throw RuntimeException("body is null")
                    try {
                        callback.onSuccess(body)
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(Exception(t))
                }
            })
    }

override suspend fun save(post: Post): Post {
        try {
            val postsResponse = PostApi.retrofitService.save(post)
            if (!postsResponse.isSuccessful) {
                throw RuntimeException(postsResponse.errorBody()?.string())
            }
            val posts = postsResponse.body() ?: throw java.lang.RuntimeException("body is null")
            dao.insert(PostEntity.fromDto(post))
        } catch (e :Exception) {
            throw Error("error")
        }
        return TODO("Provide the return value")
    }
     */
    override suspend fun likeById(post: Post) {
        dao.likeById(post.id)
        try {
            val postResponse = if (!post.likedByMe) {
                PostApi.retrofitService.likeById(post.id)
            } else {
                PostApi.retrofitService.dislikeById(post.id)
            }
        } catch (e: Exception) {
            throw Error("error")
        }
    }

    override suspend fun unLikeById(post: Post) {
        TODO()
    }
    /*= dao.likeById(id) {
        dao.likeById(id)
        posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
            )
        }
        //dao.likeById(id)
        data.value = posts
    }

    override fun unLikeByIdAsync(id: Long, callback: PostRepository.Callback<Post>) {
        PostApi.service.unLikePost(id)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    val body =
                        response.body() ?: throw RuntimeException("body is null")
                    try {
                        callback.onSuccess(body)
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(Exception(t))
                }
            })
    }*/
    /*override fun shareById(id: Long) {} = dao.sharedById(id)  {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                shareByMe = !it.shareByMe,
                sharedCount = it.sharedCount + 1
            )
        }
        data.value = posts
    }*/

//}

    /*override fun onEdit(post: Post) {}

     */

    /*override fun getByIdAsync(id: Post, callback: PostRepository.Callback<Post>) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts$id")
            .get()
            .build()

        clien.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {

                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError()
                }

            })
    }*/
}