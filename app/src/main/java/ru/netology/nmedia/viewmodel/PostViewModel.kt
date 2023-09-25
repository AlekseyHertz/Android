package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.repository.SingleLiveEvent

private val empty = Post(
    id = 0,
    content = "",
    authorId = "",
//    authorAvatar = "",
    likedByMe = false,
    published = "",
    likes = 0,
//    sharedCount = 0,
//    shareByMe = false,
//    viewsCount = 0,
//    viewByMe = false,
//    videoUrl = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryImpl()

    private val _data = MutableLiveData(FeedModel())

    //private val repository: PostRepository = PostRepositoryFileImpl(application)
    val data: LiveData<FeedModel>
        get() = _data //repository.getAll()
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    /*fun loadPost(){
        thread {
            _data.postValue(FeedModel(loading = true))
            try {
                val data = repository.getAll()
                FeedModel(posts = data, empty = data.isEmpty())
            }catch (e: Exception) {
                FeedModel(error = true)
            }.also{
                _data.postValue(it)
            }
        }
    }*/
    fun loadPosts() = viewModelScope.launch {
        try {
            _data.value = FeedModel(loading = true)
            repository.getAll()
            _data.value = FeedModel()
        } catch (e: Exception) {
            _data.value = FeedModel(error = true)
        }
    }

    /*fun loadPosts() {
        _data.postValue(FeedModel(loading = true))
        repository.getAllAsync(object : PostRepository.Callback<List<Post>> {
            override fun onSuccess(post: List<Post>) {
                _data.value = FeedModel(posts = post, empty = post.isEmpty())
            }

            override fun onError(e: Exception) {
                _data.value = FeedModel(error = true)
            }
        })
    }*/
    fun likeById(id: Post) = viewModelScope.launch {
        try {
            _data.value = FeedModel(refreshing = true)
            repository.likeById(id)
            _data.value = FeedModel()
        } catch (e: Exception) {
            _data.value = FeedModel(error = true)
        }
    }

    fun unLikeById(id: Post) = viewModelScope.launch {
        try {
            _data.value = FeedModel(refreshing = true)
            repository.unLikeById(id)
            _data.value = FeedModel()
        } catch (e: Exception) {
            _data.value = FeedModel(error = true)
        }
    }
    /*fun likeById(id: Long) {
        //val updated = if (post.likedByMe) {
        repository.likeByIdAsync(id, object : PostRepository.Callback<Post> {
            override fun onSuccess(post: Post) {
                _data.value = FeedModel(posts =
                _data.value!!.posts.map {
                    if (post.id == it.id) {
                        post.copy(likedByMe = post.likedByMe, likes = post.likes)
                    } else {
                        it
                    }
                })
            }

            override fun onError(e: Exception) {
                _data.value = FeedModel(error = true)
            }
        })
    }

    fun unLikeById(id: Long) {
        repository.unLikeByIdAsync(id, object : PostRepository.Callback<Post> {
            override fun onSuccess(post: Post) {
                _data.value = FeedModel(posts =
                _data.value!!.posts.map {
                    if (post.id == it.id) {
                        post.copy(likedByMe = post.likedByMe, likes = post.likes)
                    } else {
                        it
                    }
                })
            }

            override fun onError(e: Exception) {
                _data.value = FeedModel(error = true)
            }
        })
    }*/

    /*fun likeById(post: Post) {
        thread {
            try {
                val updated = if (post.likedByMe) {
                    repository.unLikeById(post.id)
                } else {
                    repository.likeById(post.id)
                }
                val newPosts = _data.value?.posts?.map {
                    if (it.id == post.id) {
                        updated
                    } else {
                        it
                    }
                }.orEmpty()
                _data.postValue(_data.value?.copy(posts = newPosts))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

     */


    //fun shareById(id: Long) = repository.shareById(id)
    //fun viewById(id: Long) = repository.viewById(id)
    fun edit(post: Post) {
        edited.value = post
    }

    fun removeById(id: Long) = viewModelScope.launch {
        try {
            _data.value = FeedModel(refreshing = true)
            repository.removeById(id)
            _data.value = FeedModel()
        } catch (e: Exception) {
            _data.value = FeedModel(error = true)
        }
    }
    /*fun removeById(id: Long) {
        val old = _data.value
        repository.removeByIdAsync(id, object : PostRepository.Callback<Unit> {
            override fun onSuccess(post: Unit) {
                try {
                    _data.postValue (
                        old?.copy(posts = old.posts.filter {
                            it.id != id
                        })
                    )
                } catch (e: Exception) {
                    _data.postValue (old)
                }
            }

            override fun onError(e: Exception) {
                _data.postValue(old)
            }
        })
    }*/
    /*fun removeById(id: Long) {
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
     */

    /*fun save() {
        val editPost = edited.value
        if (editPost != null) {
            edited.value?.let {
                repository.saveAsync(editPost, object : PostRepository.Callback<Unit> {
                    override fun onSuccess(post: Unit) {
                        //_data.postValue(FeedModel())
                        _postCreated.postValue(Unit)
                    }

                    override fun onError(e: Exception) {
                        _data.postValue(FeedModel(error = true))
                    }
                })
            }
            edited.postValue(empty)
        }
    }*/

    fun save() {
        edited.value?.let {
            _postCreated.value = Unit
            viewModelScope.launch {
                try {
                    repository.save(it)
                    _data.value = FeedModel()
                } catch (e: Exception) {
                    _data.value = FeedModel(error = true)
                }
            }
        }
    }
    /*fun save() {
        thread {
            edited.value?.let {
                repository.save(it)
                _postCreated.postValue(Unit)
            }
            edited.postValue(empty)
        }
    }*/


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