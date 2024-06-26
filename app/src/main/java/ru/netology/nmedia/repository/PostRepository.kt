package ru.netology.nmedia.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.dto.AttachmentType
import ru.netology.nmedia.dto.FeedItem
import ru.netology.nmedia.dto.Media
import ru.netology.nmedia.dto.MediaUpload
import ru.netology.nmedia.dto.Post

interface PostRepository {
    val data: Flow<PagingData<FeedItem>>
    fun getNewerCount(id: Long): Flow<Int>
    suspend fun getById(id: Long)
    suspend fun save(post: Post)
    suspend fun saveWithAttachment(post: Post, upload: MediaUpload, type: AttachmentType)
    suspend fun removeById(id: Long)
    suspend fun likeById(post: Post)
    suspend fun updateFeed()
    suspend fun upload(upload: MediaUpload): Media
}