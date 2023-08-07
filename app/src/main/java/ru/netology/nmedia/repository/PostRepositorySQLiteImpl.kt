package ru.netology.nmedia.repository // из PostRepositoryFileImpl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post

class PostRepositorySQLiteImpl(private val dao : PostDao) : PostRepository {

//    private val gson = Gson()//.newBuilder().setPrettyPrinting().create()
//    private val filename = "posts.json"
//    private val typeToken = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private var posts = emptyList<Post>()
    private var nextId = (posts.maxByOrNull { it.id }?.id ?: 0L) + 1
    private val data = MutableLiveData(posts)

    init {
        posts = dao.getAll()
        data.value = posts
    }

    override fun getAll(): LiveData<List<Post>> = data
    override fun likeById(id: Long) {
        dao.likeById(id)
        posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
            )
        }
        data.value = posts
    }

    override fun shareById(id: Long) {

        posts = posts.map {
            if (it.id != id) it else it.copy(
                shareByMe = !it.shareByMe,
                sharedCount = it.sharedCount + 1
            )
        }
        data.value = posts
    }

    override fun viewById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                viewsCount = it.viewsCount + 1
            )
        }
        data.value = posts
    }

    override fun onEdit(post: Post) {
        TODO("Not yet implemented")
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
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
            return
        }
        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }
        data.value = posts
        return
    }

    override fun abortText(post: Post) {
        TODO()
    }

    override fun playVideo(post: Post) {
        TODO()
    }
}