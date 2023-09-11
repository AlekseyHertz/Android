package ru.netology.nmedia.repository // из PostRepositoryFileImpl

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.dto.Post
import java.io.IOException
import java.util.concurrent.TimeUnit

class PostRepositoryImpl : PostRepository {
    private val client = OkHttpClient.Builder()
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
    private val typeToken = object :
        TypeToken<List<Post>>() {}//TypeToken.getParameterized(List::class.java, Post::class.java).type

    companion object {
        val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
        val mediaType = "application/json".toMediaType()
    }

    /*override fun getAll(): List<Post> {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

        return client.newCall(request)
            .execute()
            .let {
                it.body?.string() ?: throw RuntimeException("body is null")
            }
            .let {
                gson.fromJson(it, typeToken.type)
            }
    }*/

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

    /*init {
    }
        posts = dao.getAll()
        data.value = posts
    }*/

    /*override fun getAll(): LiveData<List<Post>> =
        dao.getAll().map { list -> list.map { it.toDto() } }
*/
    override fun likeByIdAsync(id: Long, callback: PostRepository.Callback<Post>) {
        val request: Request = Request.Builder()
                .post(gson.toJson("").toRequestBody(jsonType))//EMPTY_REQUEST)
                .url("${BASE_URL}/api/slow/posts/$id/likes")
                .build()
        /*} else {
            if (post.likes > 0) {
                Request.Builder()
                    .delete(gson.toJson(id).toRequestBody(jsonType))
                    .url("${BASE_URL}/api/posts/$id/likes")
                    .build()
            } else return
        }*/
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val body =
                        response.body?.string() ?: throw RuntimeException("body is null")
                    try {
                        callback.onSuccess(gson.fromJson(body, Post::class.java))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }
    /*override fun likeById(id: Long): Post {
        val request = Request.Builder()
            .post(EMPTY_REQUEST)
            .url("${BASE_URL}/api/slow/posts/$id/likes")
            .build()

        return client.newCall(request)
            .execute()
            .body?.string()
            ?.let {
                gson.fromJson(it, Post::class.java)
            } ?: throw RuntimeException("body is null")
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
     */

    override fun unLikeByIdAsync(id: Long, callback: PostRepository.Callback<Post>) {
        val request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id/likes")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val body =
                        response.body?.string() ?: throw RuntimeException("body is null")
                    try {
                        callback.onSuccess(gson.fromJson(body, Post::class.java))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }
    /*override fun unLikeById(id: Long): Post {
        val request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id/likes")
            .build()

        return client.newCall(request)
            .execute()
            .body?.string()
            ?.let {
                gson.fromJson(it, Post::class.java)
            } ?: throw RuntimeException("body is null")
    }

     */

    override fun shareById(id: Long) {}/* = dao.sharedById(id)  {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                shareByMe = !it.shareByMe,
                sharedCount = it.sharedCount + 1
            )
        }
        data.value = posts
    }*/

    override fun viewById(id: Long) {}/*= dao.viewById(id) //{
    posts = posts.map {
        if (it.id != id) it else it.copy(
            viewsCount = it.viewsCount + 1
        )
    }
    data.value = posts

     */
    //}

    override fun onEdit(post: Post) {
        TODO("Not yet implemented")
    }

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
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/posts$id")
            .delete()
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string() ?: throw RuntimeException("body is null")
                    callback.onSuccess(Unit)
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

            })
    }

    /*override fun removeById(id: Long) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts$id")
            .delete()
            .build()

        clien.newCall(request)
            .execute()
            .close()
    }
     */
    /*= dao.removeById(id) {
        dao.removeById(id)
        posts = posts.filter { it.id != id }
        data.value = posts
    }*/

    /*override fun save(post: Post): Post {
        val request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .post(gson.toJson(post).toRequestBody(mediaType))
            .build()

        return clien.newCall(request)
            .execute()
            .let {
                it.body?.string()
            }.let {
                gson.fromJson(it, Post::class.java)
            }
    }
     */

    override fun saveAsync(post: Post, callback: PostRepository.Callback<Unit>) {
        val request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .post(gson.toJson(post).toRequestBody(mediaType))
            .build()
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string() ?: error("body is null")
                    try {
                        callback.onSuccess(Unit)//gson.fromJson(body, Post::class.java))
                    } catch (e: java.lang.Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
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

    override fun abortText(post: Post) {
        TODO()
    }

    override fun playVideo(post: Post) {
        TODO()
    }
}