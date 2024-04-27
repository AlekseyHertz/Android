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
import ru.netology.nmedia.dto.AttachmentType
import ru.netology.nmedia.dto.FeedItem
import ru.netology.nmedia.dto.MediaModel
import ru.netology.nmedia.dto.MediaUpload
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.model.PhotoModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.File
import javax.inject.Inject
import kotlin.random.Random

private val empty = Post(id = 0)
private val noMedia = MediaModel()

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
        .authStateFlow
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

    private val _dataState = MutableLiveData<FeedModelState>()

    private val _state = MutableLiveData<FeedModelState>()
    val state: LiveData<FeedModelState>
        get() = _state

    private val _photo = MutableLiveData<PhotoModel?>()
    val photo: LiveData<PhotoModel?>
        get() = _photo

    private val _media = MutableLiveData(noMedia)
    val media: LiveData<MediaModel>
        get() = _media


    private val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

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
            viewModelScope.launch {
                val post = requireNotNull(edited.value)
                try {
                    _dataState.value = FeedModelState(loading = true)
                    when (_media.value) {
                        noMedia -> {
                            repository.save(post)
                        }

                        else -> {
                            when (_media.value?.type) {
                                AttachmentType.IMAGE -> {
                                    _media.value?.file?.let { file ->
                                        repository.saveWithAttachment(
                                            post, MediaUpload(file), AttachmentType.IMAGE
                                        )
                                    }
                                }

                                AttachmentType.VIDEO -> {
                                    _media.value?.file?.let { file ->
                                        repository.saveWithAttachment(
                                            post,
                                            MediaUpload(file),
                                            AttachmentType.VIDEO
                                        )
                                    }
                                }

                                AttachmentType.AUDIO -> {
                                    _media.value?.file?.let { file ->
                                        repository.saveWithAttachment(
                                            post, MediaUpload(file), AttachmentType.AUDIO
                                        )
                                    }
                                }

                                null -> repository.save(post)
                            }
                        }
                    }
                    _dataState.value = FeedModelState()
                } catch (e: Exception) {
                    _dataState.value = FeedModelState(error = true)
                } finally {
                    clearPhoto()
                }
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

    fun changeMedia (uri: Uri?, file: File?, type: AttachmentType?) {
        _media.value = MediaModel(uri, file, type)
    }
}
