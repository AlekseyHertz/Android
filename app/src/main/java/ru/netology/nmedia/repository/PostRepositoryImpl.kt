package ru.netology.nmedia.repository // из PostRepositoryFileImpl

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.PostApi
import ru.netology.nmedia.dto.Post

class PostRepositoryImpl : PostRepository {
    /*  убрали фрагмент после внесения api */

    /*private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        })
        .build()


    private val gson = Gson()//.newBuilder().setPrettyPrinting().create()
    private val typeToken = object : TypeToken<List<Post>>() {}//TypeToken.getParameterized(List::class.java, Post::class.java).type

    companion object {
        val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
        val mediaType = "application/json".toMediaType()
    }*/

    /*  убрали фрагмент после внесения api */

    override fun getAll(): List<Post> {
        return PostApi.service.getAll()
            .execute()
            .let {
                it.body() ?: throw RuntimeException("body is null")
            }
    }

    override fun getById(id: Long) {
        PostApi.service.getById(id)
            .execute()
    }

    override fun getAllAsync(callback: PostRepository.Callback<List<Post>>) {
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


    /*override fun getAllAsync(callback: PostRepository.Callback<List<Post>>) {
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
    */
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


    override fun likeById(id: Long) {
        PostApi.service.likePost(id)
            .execute()
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
    }*/

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
    }

    override fun unLikeById(id: Long) {
        PostApi.service.unLikePost(id)
            .execute()
    }

    /*override fun shareById(id: Long) {} = dao.sharedById(id)  {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                shareByMe = !it.shareByMe,
                sharedCount = it.sharedCount + 1
            )
        }
        data.value = posts
    }*/

    /*override fun viewById(id: Long) {}= dao.viewById(id) //{
    posts = posts.map {
        if (it.id != id) it else it.copy(
            viewsCount = it.viewsCount + 1
        )
    }
    data.value = posts

     */
//}

    /*override fun onEdit(post: Post) {
        TODO("Not yet implemented")
    }

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
    }
     */

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
    }

    override fun removeById(id: Long) {
        PostApi.service.deletePost(id)
            .execute()
    }

    /*= dao.removeById(id) {
        dao.removeById(id)
        posts = posts.filter { it.id != id }
        data.value = posts
    }*/

    override fun save(post: Post) {
        PostApi.service.savePost(post)
            .execute()
    }


    override fun saveAsync(post: Post, callback: PostRepository.Callback<Unit>) {
        PostApi.service.savePost(post)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    val body = response.body() ?: error("body is null")
                    try {
                        callback.onSuccess(Unit)//gson.fromJson(body, Post::class.java))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(Exception(t))
                }
            })
    }
}


/*= dao.save(PostEntity.fromDto(post)) {
    val id = post.id
    val saved = dao.save(post)
    posts = if (post.id == 0L) {
        listOf(saved) + posts
    } else {
        posts.map {
            if (it.id != post.id) it else saved
        }
    }
    dao.save(post)
    data.value = posts
}
    dao.save(post)
    if (post.id == 0L) {
        posts = listOf(
            post.copy(
                id = nextId++,
                author = "Me",
                likedByMe = false,
                published = "now"
            )
        ) + posts
        data.value = posts
        dao.save(post)
        return
    }
    posts = posts.map {
        if (it.id != post.id) it else it.copy(content = post.content)
    }
    data.value = posts
    dao.save(post)
    return
}*/

/*override fun abortText(post: Post) {
    TODO()
}

override fun playVideo(post: Post) {
    TODO()
}*/