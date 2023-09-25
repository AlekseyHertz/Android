package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val authorId: String,
//    val authorAvatar: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean = false,
    val likes: Int = 0,
//    val sharedCount: Int = 0,
//    val shareByMe: Boolean = false,
//    val viewsCount: Int = 0,
//    val viewByMe: Boolean = false,
//    val videoUrl: String? = null,
//    var attachment: Attachment? = null
) {
    fun toDto() = Post(
        id,
        authorId,
//        authorAvatar,
        content,
        published,
        likedByMe,
        likes
//        sharedCount,
//        shareByMe,
//        viewsCount,
//        viewByMe,
//        videoUrl!!
//        attachment = Attachment("","","")
    )

    companion object {
        fun fromDto(dto: Post) = PostEntity(
            dto.id,
            dto.authorId,
//            dto.authorAvatar,
            dto.content,
            dto.published,
            dto.likedByMe,
            dto.likes
//            dto.sharedCount,
//            dto.shareByMe,
//            dto.viewsCount,
//            dto.viewByMe,
//            dto.videoUrl
        )
    }
}