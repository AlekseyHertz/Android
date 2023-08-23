package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.repository.SingleLiveEvent
import kotlin.concurrent.thread

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    published = "",
    likes = 0,
    sharedCount = 0,
    shareByMe = false,
    viewsCount = 0,
    viewByMe = false,
    videoUrl = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryImpl(
        AppDb.getInstance(application).postDao
    )

    private val _data = MutableLiveData(FeedModel())

    //private val repository: PostRepository = PostRepositoryFileImpl(application)
    val data: LiveData<FeedModel> = _data //repository.getAll()
    val edited = MutableLiveData(empty)

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit> = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() {
        thread {
            _data.postValue(FeedModel(loading = true))
            try {
                val data = repository.getAll()
                FeedModel(posts = data, empty = data.isEmpty())
            } catch (e: Exception) {
                FeedModel(error = true)
            }.also {
                _data.postValue(it)
            }
        }
    }

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun viewById(id: Long) = repository.viewById(id)
    fun edit(post: Post) {
        edited.value = post
    }

    fun removeById(id: Long) {
        thread {
            val old = _data.value

            _data.postValue(
                old?.copy(
                    posts = old.posts.filter {
                        it.id != id
                    }
                )
            )
            try {
                repository.removeById(id)
            } catch (e: Exception) {
                _data.postValue(old)
            }
        }
    }

    fun save() {
        thread {
            edited.value?.let {
                repository.save(it)
                _postCreated.postValue(Unit)
            }
            edited.postValue(empty)
        }
    }

    fun changeContent(content: String) {
        edited.value?.let {
            val text = content.trim()
            if (it.content == text) {
                return
            }
            edited.value = it.copy(content = text)
        }
    }

    fun abortText() {
        edited.value?.let {
            edited.value = empty
        }
    }
}