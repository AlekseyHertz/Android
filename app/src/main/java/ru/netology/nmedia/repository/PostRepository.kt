package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(): List<Post>
    fun getById (id: Long)
    fun save (post: Post)
    fun removeById(id: Long)
    fun likeById(id: Long)
    fun unLikeById(id: Long)
//    fun shareById(id: Long)
//    fun viewById(id: Long)
//    fun onEdit(post: Post)
//    fun abortText(post: Post)
//    fun playVideo(post: Post) {}
    fun getAllAsync(callback: Callback<List<Post>>)
    fun saveAsync(post: Post, callback: Callback<Unit>)
//    fun getByIdAsync(id: Post, callback: Callback<Post>)
    fun likeByIdAsync(id: Long, callback: Callback<Post>)
    fun unLikeByIdAsync(id: Long, callback: Callback<Post>)
    fun removeByIdAsync(id: Long, callback: Callback<Unit>)

    interface Callback<T> {
        fun onSuccess(post: T)
        fun onError(e: Exception)
    }
}