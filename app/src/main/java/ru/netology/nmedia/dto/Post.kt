package ru.netology.nmedia.dto

import android.annotation.SuppressLint
import android.icu.text.DecimalFormat
import android.os.Build
import androidx.annotation.RequiresApi

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likes: Int = 999,
    var likedByMe: Boolean = false,
    var sharedCount: Int = 99_999,
    var shareByMe: Boolean = false
)

@RequiresApi(Build.VERSION_CODES.N)
@SuppressLint("DefaultLocale")
fun countScore(count: Int): String {
    return when (count) {
        in 1_000..999_999 -> "${formatCount(count.toDouble() / 1_000)}K"
        in 1_000_000..999_999_999 -> "${formatCount(count.toDouble() / 1_000_000)}M"
        else -> count.toString()
    }
}

@RequiresApi(Build.VERSION_CODES.N)
fun formatCount(count: Double): Double {
    val i = DecimalFormat("#.#")
    return i.format(count).toDouble()
}

/*fun countFormat(count: Int): String {
    val i = count / 1000
    return format("%.1f", i)
}

fun countFormatLong (count: Int) :String {
    val y = count / 1_000_000
    return format("%.1f", y)
}

fun shareScore(sharedCount: Int): String {
    return when (sharedCount) {
        in 1000..1099 -> "1k"
        in 1100..1199 -> "1.1k"
        in 1200..1299 -> "1.2k"
        in 1300..1399 -> "1.3k"
        in 1400..1499 -> "1.4k"
        in 1500..1599 -> "1.5k"
        in 1600..1699 -> "1.6k"
        in 1700..1799 -> "1.7k"
        in 1800..1899 -> "1.8k"
        in 1900..1999 -> "1.9k"
        else -> sharedCount.toString()
    }
}

fun likesScore(likes: Int): String {
    return when (likes) {
        in 1000..1099 -> "1k"
        in 1100..1199 -> "1.1k"
        in 1200..1299 -> "1.2k"
        in 1300..1399 -> "1.3k"
        in 1400..1499 -> "1.4k"
        in 1500..1599 -> "1.5k"
        in 1600..1699 -> "1.6k"
        in 1700..1799 -> "1.7k"
        in 1800..1899 -> "1.8k"
        in 1900..1999 -> "1.9k"
        else -> likes.toString()
    }
}


        in 1_000..999_999 -> "${format("%1f", count / 1_000)}K"
        in 1_000_000..999_999_999 -> "${format("%1f", count / 1_000_000)}M"
        else -> count.toString()
        */