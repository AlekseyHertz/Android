package ru.netology.nmedia.dto

/*data class Post(
    val id: Long,
    val author: String,
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
)
data class Attachment (
    val url: String = "",
    val description: String = "",
    val type : String = ""
)*/
data class Post(
    val id: Long,
    val authorId: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    var attachment: Attachment? = null,
)

data class Attachment(
    val url: String,
    val description: String,
    val type: String
)

data class Comment(
    val id: Long,
    val postId: Long,
    val authorId: Long,
    val content: String,
    val published: Long,
    val likedByMe: Boolean,
    val likes: Int = 0,
)

data class Author(
    val id: Long,
    val name: String,
    val avatar: String,
)

fun convertCount(element: Int) =
    when (element) {
        in 0..999 -> element.toString()
        in 1_000..99_999 -> String.format("%.1fK", (element / 100 * 100).toDouble() / 1000)
        in 100_000..999_999 -> "${element / 1_000}K"
        else -> String.format("%.1fM", (element / 100_000 * 100_000).toDouble() / 1_000_000)
    }