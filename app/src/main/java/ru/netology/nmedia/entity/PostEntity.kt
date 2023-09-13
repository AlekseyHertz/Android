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
    val likes: Int = 0,
    val likedByMe: Boolean = false,
    val sharedCount: Int = 0,
    val shareByMe: Boolean = false,
    val viewsCount: Int = 0,
    val viewByMe: Boolean = false,
    val videoUrl: String? = null,
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
        videoUrl!!
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