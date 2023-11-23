package ru.netology.nmedia.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.dto.MediaUpload
import ru.netology.nmedia.dto.Post

interface PostRepository {
    val data: Flow<PagingData<Post>>
//    val data: Flow<List<Post>>
    fun getNewerCount (id: Long): Flow<Int>
    suspend fun getAll()
    suspend fun getById(id: Long)
    suspend fun save(post: Post, upload: MediaUpload?)

    //suspend fun saveWithAttachment(post: Post, upload: MediaUpload)
    suspend fun removeById(id: Long)
    suspend fun likeById(post: Post)
    suspend fun unLikeById(post: Post)

//    fun shareById(id: Long)
//    fun viewById(id: Long)
//    fun onEdit(post: Post)
//    fun abortText(post: Post)
//    fun playVideo(post: Post) {}
//    fun getAllAsync(callback: Callback<List<Post>>)
//    fun saveAsync(post: Post, callback: Callback<Unit>)
//    fun getByIdAsync(id: Post, callback: Callback<Post>)
//    fun likeByIdAsync(id: Long, callback: Callback<Post>)
//    fun unLikeByIdAsync(id: Long, callback: Callback<Post>)
//    fun removeByIdAsync(id: Long, callback: Callback<Unit>)

    /*interface Callback<T> {
        fun onSuccess(post: T)
        fun onError(e: Exception)
    }
     */
}