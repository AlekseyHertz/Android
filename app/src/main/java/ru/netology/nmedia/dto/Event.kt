package ru.netology.nmedia.dto

data class Event(
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String,
    val authorJob: String?,
    val content: String,
    val datetime: String,
    val published: String,
    val coords: Coordinates?,
    val type: EventType?,
    val likeOwnerIds: List<Int>,
    val likedByMe: Boolean,
    val speakerIds: List<Int>,
    val participantsIds: List<Int>,
    val participatedByMe: Boolean,
    val attachment: Attachment?,
    val link: String?,
    val users: UserPreview?
)

data class EventRequest(
    val id: Int = 0,
    val content: String = "",
    val datetime: String? = null,
    val coords: Coordinates? = null,
    val type: EventType? = EventType.OFFLINE,
    val attachment: Attachment? = null,
    val link: String? = null,
    val speakerIds: List<Int>? = null,
)

data class Coordinates(
    val lat: Double,
    val long: Double
)

enum class EventType {
    OFFLINE,
    ONLINE
}

data class UserPreview (
    val name: String,
val avatar: String
)
