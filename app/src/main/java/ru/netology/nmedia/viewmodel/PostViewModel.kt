package ru.netology.nmedia.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.dto.Ad
import ru.netology.nmedia.dto.FeedItem
import ru.netology.nmedia.dto.MediaUpload
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.model.PhotoModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.File
import javax.inject.Inject
import kotlin.random.Random

private val empty = Post(id = 0
//    id = 0,
//    authorId = 0,
//    content = "",
//    author = "",
//    authorAvatar = "",
//    likedByMe = false,
//    published = 0,
//    likes = 0,
//    sharedCount = 0,
//    shareByMe = false,
//    viewsCount = 0,
//    viewByMe = false,
//    videoUrl = ""
)

private val noPhoto = PhotoModel()

@HiltViewModel
@ExperimentalCoroutinesApi
class PostViewModel @Inject constructor(
    private val repository: PostRepository,
    private val appAuth: AppAuth,
) : ViewModel() {
    private val cached: Flow<PagingData<FeedItem>> = repository
        .data
        .map { pagingData ->
            pagingData.insertSeparators(
                generator = { before, _ ->
                    if (before == null) {
                        return@insertSeparators null
                    }

                    if (before.id.rem(5) == 0L) {
                        Ad(Random.nextLong(), "figma.jpg")
                    } else
                        null
                }
            )
        }
        .cachedIn(viewModelScope)

    val data: Flow<PagingData<FeedItem>> = appAuth
        ._authStateFlow
        .flatMapLatest { (myId, _) ->
            cached.map { pagingData ->
                pagingData.map { post ->
                    if (post is Post) {
                        post.copy(ownerByMe = post.authorId == myId)
                    } else {
                        post
                    }
                }
            }.flowOn(Dispatchers.Default)
        }


    /*val data: LiveData<FeedModel> = appAuth
        ._authStateFlow
        .flatMapLatest { (myId, _) ->
            repository.data
                .map { posts ->
                    FeedModel(
                        posts.map {
                            it.copy(ownerByMe = it.authorId == myId)
                        },
                        posts.isEmpty()
                    )
                }
        }
        .asLiveData(Dispatchers.Default)*/


    private val _dataState = MutableLiveData<FeedModelState>()

    /*val newerCount: LiveData<Int> = data.switchMap {
        repository.getNewerCount(it.posts.firstOrNull()?.id ?: 0L)
            .catch { e -> e.printStackTrace() }
            .asLiveData(Dispatchers.Default)
    }*/

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

    /*init {
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            try {
                _state.value = FeedModelState(loading = true)
                _state.value = FeedModelState()
            } catch (e: Exception) {
                _state.value = FeedModelState(error = true)
            }
        }
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
    }*/

    fun setPhoto(uri: Uri, file: File) {
        _photo.value = PhotoModel(uri, file)
    }

    fun clearPhoto() {
        _photo.value = PhotoModel()
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                _state.value = FeedModelState(loading = true)
                _state.value = FeedModelState()
            } catch (e: Exception) {
                _state.value = FeedModelState(error = true)
            }
        }
    }

    /*fun refresh() {
        viewModelScope.launch {
            _state.value = FeedModelState(refreshing = true)
            _state.value = try {
                repository.getAll()
                FeedModelState()
            } catch (e: Exception) {
                FeedModelState(error = true)
            }
        }
    }*/

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

    /*fun dislikeById(post: Post) {
        viewModelScope.launch {
            try {
                _state.value = FeedModelState(loading = true)
                repository.unLikeById(post)
                _state.value = FeedModelState()
            } catch (e: Exception) {
                _state.value = FeedModelState(error = true)
            }
        }
    }*/

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

    fun save() {
        edited.value?.let {post ->
            viewModelScope.launch {
                try {
                    photo.value?.file?.let {
                        repository.saveWithAttachment(post, MediaUpload(it))
                    } ?: repository.save(post)
                    _postCreated.value = Unit
                    edited.value = empty
                    _dataState.value = FeedModelState()
                } catch (e: Exception) {
                    _dataState.value = FeedModelState(error = true)
                }
            }
        }
    }

    /*fun save() {
        edited.value?.let {
            _postCreated.value = Unit
            viewModelScope.launch {
                try {
                    when (_photo.value) {
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

    /*fun abortText() {
        edited.value?.let {
            edited.value = empty
        }
    }*/
}
