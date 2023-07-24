package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun likeById (id: Long)
    fun shareById (id: Long)
    fun viewById (id: Long)
    fun onEdit(post: Post)
    fun removeById (id: Long)
    fun save (post: Post)
    fun abortText (post: Post)

    fun playVideo (post: Post) {}
}