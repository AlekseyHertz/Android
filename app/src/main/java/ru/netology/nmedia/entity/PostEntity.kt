package ru.netology.nmedia.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val authorId: Long,
    val authorAvatar: String?,
    val content: String,
    val published: String, // Long,
    val likes: Int,
    val likedByMe: Boolean,
    val sharedCount: Int,
    val shareByMe: Boolean,
    val viewsCount: Int,
    val viewByMe: Boolean,
    @Embedded
    var attachment: AttachmentEmbeddable?,
    val hidden: Boolean = false,
    val ownerByMe: Boolean = false


) {
    fun toDto() = Post(
        id = id,
        author = author,
        authorId = authorId,
        authorAvatar = authorAvatar,
        content = content,
        published = published,
        likes = likes,
        likedByMe = likedByMe,
        sharedCount = sharedCount,
        shareByMe = shareByMe,
        viewsCount = viewsCount,
        viewByMe = viewByMe,
        attachment = attachment?.toDto(),
        hidden = hidden,
        ownerByMe = ownerByMe
    )

    companion object {
        fun fromDto(dto: Post) = PostEntity(
            dto.id,
            dto.author,
            dto.authorId,
            dto.authorAvatar,
            dto.content,
            dto.published,
            dto.likes,
            dto.likedByMe,
            dto.sharedCount,
            dto.shareByMe,
            dto.viewsCount,
            dto.viewByMe,
            AttachmentEmbeddable.fromDto(dto.attachment),
            //dto.attachment,
            dto.hidden,
            dto.ownerByMe
        )
    }
}

fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)

