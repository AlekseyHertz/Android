package ru.netology.nmedia.dto

import android.net.Uri
import java.io.File

class Media (val url: String)
data class MediaUpload(val file: File)

data class MediaModel(
    val uri: Uri? = null,
    val file: File? = null,
    val type: AttachmentType? = null,
)