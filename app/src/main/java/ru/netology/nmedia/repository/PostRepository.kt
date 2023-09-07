package ru.netology.nmedia.repository

import okhttp3.Callback
import ru.netology.nmedia.dto.Post

interface PostRepository {
    //fun getAll(): List<Post>
    //fun likeById(id: Long): Post
    //fun unLikeById(id: Long): Post
    fun shareById(id: Long)
    fun viewById(id: Long)
    fun onEdit(post: Post)
    //fun removeById(id: Long)

    //fun save (post: Post) : Post
    fun abortText(post: Post)
    fun playVideo(post: Post) {}

    fun getAllAsync(callback: Callback<List<Post>>)
    fun saveAsync(post: Post, callback: Callback<Post>)
    fun getByIdAsync(id: Post, callback: Callback<Post>)
    fun likeByIdAsync(id: Post, callback: Callback<Post>)
    fun unLikeByIdAsync(id: Post, callback: Callback<Post>)
    fun removeByIdAsync(id: Post, callback: Callback<Post>)

    interface Callback<T> {
        fun onSuccess(post: T)
        fun onError()
    }
}