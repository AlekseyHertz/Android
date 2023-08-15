package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
class PostEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likes: Int,
    val likedByMe: Boolean = false,
    val sharedCount: Int,
    val shareByMe: Boolean = false,
    val viewsCount: Int,
    val viewByMe: Boolean = false,
    val videoUrl: String
) {
    fun toDto() = Post (
        id = id, author = author, content = content, published = published, likes = likes, likedByMe = likedByMe, sharedCount = sharedCount, shareByMe = shareByMe, viewsCount = viewsCount, viewByMe = viewByMe, videoUrl = videoUrl
            )

    companion object {
        fun fromDto (post: Post) = PostEntity (
            id = post.id, author = post.author, content = post.content, published = post.published, likes = post.likes, likedByMe = post.likedByMe, sharedCount = post.sharedCount, shareByMe = post.shareByMe, viewsCount = post.viewsCount, viewByMe = post.viewByMe, videoUrl = post.videoUrl
        )
    }
}