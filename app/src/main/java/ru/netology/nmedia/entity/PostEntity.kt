package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: Long, //String,
    val likes: Int,
    val likedByMe: Boolean,
    val sharedCount: Int,
    val shareByMe: Boolean,
    val viewsCount: Int,
    val viewByMe: Boolean,
    val videoUrl: String? = null,
    val hidden: Boolean = false
    //var attachment: Attachment? = null
) {
    fun toDto() = Post(
        id,
        author,
        authorAvatar,
        content,
        published,
        likes,
        likedByMe,
        sharedCount,
        shareByMe,
        viewsCount,
        viewByMe,
        videoUrl
        //attachment = Attachment("","","")
    )

    companion object {
        fun fromDto(dto: Post) = PostEntity(
            dto.id,
            dto.author,
            dto.authorAvatar,
            dto.content,
            dto.published,
            dto.likes,
            dto.likedByMe,
            dto.sharedCount,
            dto.shareByMe,
            dto.viewsCount,
            dto.viewByMe,
            dto.videoUrl
        )
    }
}