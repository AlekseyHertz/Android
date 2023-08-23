package ru.netology.nmedia.repository // из PostRepositoryFileImpl

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import java.util.concurrent.TimeUnit

class PostRepositoryImpl(
    private val dao: PostDao
) : PostRepository {

    private val clien: OkHttpClient = OkHttpClient.Builder()
        .callTimeout(60, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()//.newBuilder().setPrettyPrinting().create()

    private val typeToken = object :
        TypeToken<List<Post>>() {}//TypeToken.getParameterized(List::class.java, Post::class.java).type

    //    private val filename = "posts.json"
    //    private var posts = emptyList<Post>()
    //    private var nextId = (posts.maxByOrNull { it.id }?.id ?: 0L) + 1
    //    private val data = MutableLiveData(posts)

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
        val mediaType = "application/json".toMediaType()
    }

    override fun getAll(): List<Post> {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

        return clien.newCall(request)
            .execute()
            .let {
                it.body?.string() ?: throw RuntimeException("body is null")
            }
            .let {
                gson.fromJson(it, typeToken.type)
            }
    }
    /*init {
    }
        posts = dao.getAll()
        data.value = posts
    }*/

    /*override fun getAll(): LiveData<List<Post>> =
        dao.getAll().map { list -> list.map { it.toDto() } }
*/
    override fun likeById(id: Long) = dao.likeById(id) /*{
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

    override fun shareById(id: Long) = dao.sharedById(id) /* {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                shareByMe = !it.shareByMe,
                sharedCount = it.sharedCount + 1
            )
        }
        data.value = posts
    }*/

    override fun viewById(id: Long) = dao.viewById(id) //{
    /*posts = posts.map {
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

    override fun removeById(id: Long)  {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts$id")
            .delete()
            .build()

        clien.newCall(request)
            .execute()
    }
    /*= dao.removeById(id) {
        dao.removeById(id)
        posts = posts.filter { it.id != id }
        data.value = posts
    }*/

    override fun save(post: Post): Post {
        val request= Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .post(gson.toJson(post).toRequestBody(mediaType))
            .build()

        return clien.newCall(request)
            .execute()
            .let {
                it.body?.string() ?: error("body is null")
            }.let {
                gson.fromJson(it, Post::class.java)
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

    override fun abortText(post: Post) {
        TODO()
    }

    override fun playVideo(post: Post) {
        TODO()
    }
}