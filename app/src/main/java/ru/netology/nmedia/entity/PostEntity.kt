package ru.netology.nmedia.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val authorId: Long,
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
    val hidden: Boolean = false,
    @Embedded
    var attachment: Attachment?,
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
        videoUrl = videoUrl,
        attachment = attachment
    )

    companion object {
        fun fromDto(dto: Post) = PostEntity(
            id = dto.id,
            author = dto.author,
            authorId = dto.authorId,
            authorAvatar = dto.authorAvatar,
            content = dto.content,
            published = dto.published,
            likes = dto.likes,
            likedByMe = dto.likedByMe,
            sharedCount = dto.sharedCount,
            shareByMe = dto.shareByMe,
            viewsCount = dto.viewsCount,
            viewByMe = dto.viewByMe,
            videoUrl = dto.videoUrl,
            hidden = dto.hidden,
            attachment = dto.attachment
        )
    }
}

/*data class AttachmentEmbeddable(
    var url: String,
    var type: AttachmentType,
) {
    fun toDto() = Attachment(url,type)

    companion object {
        fun fromDto (dto: Attachment?) = dto?.let {
            AttachmentEmbeddable(it.url,it.type)
        }
    }
}


fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)
*/