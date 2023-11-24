package ru.netology.nmedia.dto


sealed interface FeedItem {
    val id: Long
}
data class Post(
    override val id: Long,
    val author: String,
    val authorId: Long,
    val authorAvatar: String,
    val content: String,
    val published: Long,
    val likes: Int,
    val likedByMe: Boolean = false,
    val sharedCount: Int,
    val shareByMe: Boolean = false,
    val viewsCount: Int,
    val viewByMe: Boolean = false,
    val videoUrl: String? = null,
    var attachment: Attachment? = null,
    val hidden: Boolean = false,
    val ownerByMe: Boolean = false
) : FeedItem

data class Ad(
    override val id: Long,
    val image: String
) : FeedItem

data class Attachment(
    val url: String,
    val type: AttachmentType
)

enum class AttachmentType{
    IMAGE
}

fun convertCount(element: Int) =
    when (element) {
        in 0..999 -> element.toString()
        in 1_000..99_999 -> String.format("%.1fK", (element / 100 * 100).toDouble() / 1000)
        in 100_000..999_999 -> "${element / 1_000}K"
        else -> String.format("%.1fM", (element / 100_000 * 100_000).toDouble() / 1_000_000)
    }