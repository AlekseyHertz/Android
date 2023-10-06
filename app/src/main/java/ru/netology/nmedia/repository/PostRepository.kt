package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    val data: LiveData<List<Post>>
    suspend fun getAll()
    suspend fun getById(id: Long)
    suspend fun save(post: Post)
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