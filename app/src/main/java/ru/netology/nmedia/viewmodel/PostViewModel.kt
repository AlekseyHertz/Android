package ru.netology.nmedia.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.MediaUpload
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.repository.SingleLiveEvent
import java.io.File

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    authorAvatar = "",
    likedByMe = false,
    published = 0,
    likes = 0,
    sharedCount = 0,
    shareByMe = false,
    viewsCount = 0,
    viewByMe = false,
    videoUrl = ""
)

private val noPhoto = PhotoModel()

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository =
        PostRepositoryImpl(AppDb.getInstance(context = application).postDao())

    //private val repository: PostRepository = PostRepositoryFileImpl(application)
    val data: LiveData<FeedModel> = repository.data
        .map(::FeedModel)
        .asLiveData(Dispatchers.Default)

    val newerCount: LiveData<Int> = data.switchMap {
        repository.getNewerCount(it.posts.firstOrNull()?.id ?: 0L)
            .catch { e -> e.printStackTrace() }
            .asLiveData(Dispatchers.Default)
    }

    private val _state = MutableLiveData<FeedModelState>()
    val state: LiveData<FeedModelState>
        get() = _state

    private val _photo = MutableLiveData<PhotoModel?>()
    val photo: LiveData<PhotoModel?>
        get() = _photo

    private val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            _state.value = FeedModelState(loading = true)
            _state.value = try {
                repository.getAll()
                FeedModelState()
            } catch (e: Exception) {
                FeedModelState(error = true)
            }
        }
    }

    fun setPhoto(uri: Uri, file: File) {
        _photo.value = PhotoModel(uri, file)
    }

    fun clearPhoto(uri: Uri?, file: File?) {
        _photo.value = PhotoModel(uri, file)
    }

    fun refresh() {
        viewModelScope.launch {
            _state.value = FeedModelState(refreshing = true)
            _state.value = try {
                repository.getAll()
                FeedModelState()
            } catch (e: Exception) {
                FeedModelState(error = true)
            }
        }
    }

    fun likeById(post: Post) {
        viewModelScope.launch {
            try {
                _state.value = FeedModelState(loading = true)
                repository.likeById(post)
                _state.value = FeedModelState()
            } catch (e: Exception) {
                _state.value = FeedModelState(error = true)
            }
        }
    }

    fun dislikeById(post: Post) {
        viewModelScope.launch {
            try {
                _state.value = FeedModelState(loading = true)
                repository.unLikeById(post)
                _state.value = FeedModelState()
            } catch (e: Exception) {
                _state.value = FeedModelState(error = true)
            }
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun removeById(id: Long) {
        viewModelScope.launch {
            try {
                _state.value = FeedModelState(loading = true)
                repository.removeById(id)
                _state.value = FeedModelState()
            } catch (e: Exception) {
                _state.value = FeedModelState(error = true)
            }

        }
    }

    /*fun save() {
        edited.value?.let {
            viewModelScope.launch {
                repository.save(it)
                _postCreated.value = Unit
            }
        }
    }*/
    fun save() {
        edited.value?.let {
            _postCreated.value = Unit
            viewModelScope.launch {
                try {
                    when(_photo.value) {
                        noPhoto -> repository.save(it)
                        else -> _photo.value?.file?.let { file ->
                            repository.saveWithAttachment(it, MediaUpload(file))
                        }
                    }
                    _state.value = FeedModelState()
                } catch (e: Exception) {
                    _state.value = FeedModelState(error = true)
                }
            }
        }
        edited.value = empty
        _photo.value = noPhoto
    }

    /*fun save() {
        edited.value?.let { post ->
            viewModelScope.launch {
                try {
                    photo.value?.let {
                        repository.saveWithAttachment(post, it)
                    } ?: repository.save(post)
                    _postCreated.value = Unit
                    edited.value = empty
                    _state.value = FeedModelState()
                } catch (e: Exception) {
                    _state.value = FeedModelState(error = true)
                }
            }
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
